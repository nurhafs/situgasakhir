package a04.tugasakhirfarmasi.service;

import a04.tugasakhirfarmasi.model.InformasiModel;
import a04.tugasakhirfarmasi.model.JadwalNonSidangModel;
import a04.tugasakhirfarmasi.model.PenggunaModel;
import a04.tugasakhirfarmasi.repository.InformasiDB;
import a04.tugasakhirfarmasi.repository.InformasiPaginationDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class InformasiServiceImpl implements InformasiService{
    @Autowired
    private InformasiDB informasiDB;

    @Autowired
    private InformasiPaginationDB informasiPageDB;

    @Qualifier("penggunaServiceImpl")
    @Autowired
    PenggunaService penggunaService;

    @Override
    public void addInformasi (MultipartFile fileInformasi, InformasiModel informasi) throws IOException {
        LocalTime now = LocalTime.now();
        Date date = new Date();
        informasi.setWaktu(now);
        informasi.setTanggal(date);
        informasi.setNamaFile(StringUtils.cleanPath(fileInformasi.getOriginalFilename()));
        informasi.setFile(fileInformasi.getBytes());
        informasiDB.save(informasi);
    }

    @Override
    public List<InformasiModel> getAllInformasi(String role, String prodi) {
        List<InformasiModel> listInformasiAll = informasiDB.findAllByOrderByIdDesc();
        List<InformasiModel> listInformasi = new ArrayList<>();
        if (role.equals("Mahasiswa")) {
            for (InformasiModel info:listInformasiAll) {
                if(info.isApoteker() && prodi.equals("Apt")) {
                    listInformasi.add(info);
                }
                else if (info.isS1() && prodi.equals("S1")) {
                    listInformasi.add(info);
                }
                else if (info.isS2() && prodi.equals("S2")) {
                    listInformasi.add(info);
                }
                else if (info.isS3() && prodi.equals("S3")) {
                    listInformasi.add(info);
                }
            }
        } else {
            return listInformasiAll;
        }

        return listInformasi;
    }

    @Override
    public void deleteInformasi(Integer id) {
        InformasiModel i = informasiDB.findById(id).get();
        informasiDB.delete(i);
    }

    @Override
    public InformasiModel getInformasiById(Integer id){
        Optional<InformasiModel> informasi = informasiDB.findById(id);
        if(informasi.isPresent()) return informasi.get();
        else return null;
    }

    @Override
    public void updateInformasi(Integer id, InformasiModel info, MultipartFile file) throws IOException {
        InformasiModel informasi = getInformasiById(id);
        informasi.setJudul(info.getJudul());
        informasi.setIsi(info.getIsi());
        informasi.setS1(info.isS1());
        informasi.setS2(info.isS2());
        informasi.setS3(info.isS3());
        informasi.setApoteker(info.isApoteker());
        informasi.setNamaFile(StringUtils.cleanPath(file.getOriginalFilename()));
        informasi.setFile(file.getBytes());
    }

    @Override
    public List<InformasiModel> findInfoPaginated(int pageNo, int pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<InformasiModel> pagedResult = informasiPageDB.findAll(paging);

        return pagedResult.toList();
    }

    @Override
    public Page<InformasiModel> findPaginated(Pageable pageable) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = String.valueOf(principal);

        PenggunaModel pengguna = penggunaService.getUserByUsername(username);
        if (pengguna == null) {
            username = ((UserDetails)principal).getUsername();
            pengguna = penggunaService.getUserByUsername(username);
        }

        String role = pengguna.getRole().getNama();
        String prodi = pengguna.getProdiPengguna().getNama();

        List<InformasiModel> listInfo = getAllInformasi(role, prodi);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<InformasiModel> list;

        if (listInfo.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, listInfo.size());
            list = listInfo.subList(startItem, toIndex);
        }

        Page<InformasiModel> infoPage
                = new PageImpl<InformasiModel>(list, PageRequest.of(currentPage, pageSize), listInfo.size());

        return infoPage;
    }

}
