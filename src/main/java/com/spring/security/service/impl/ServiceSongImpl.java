package com.spring.security.service.impl;

import com.cloudinary.Cloudinary;
import com.mpatric.mp3agic.Mp3File;
import com.spring.security.entity.SongEntity;
import com.spring.security.entity.SongSearchEntity;
import com.spring.security.model.Lyrics;
import com.spring.security.model.Song;
import com.spring.security.repository.SongRepository;
import com.spring.security.request.ListSongId;
import com.spring.security.response.song.CreateSongResponse;
import com.spring.security.response.song.DeleteSongResponse;
import com.spring.security.response.song.ListSongResponse;
import com.spring.security.response.song.UpdateSongResponse;
import com.spring.security.service.ServiceSong;
import java.nio.file.Files;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings(value = {"CallToPrintStackTrace", "ThrowablePrintedToSystemOut"})
@Service
class ServiceSongImpl implements ServiceSong {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Value("${folder.specified.name}")
    private String uploadUrl;

    @Value("${upload.dir}")
    private String uploadDir;
    private List<File> files;

    @Override
    public ResponseEntity<ListSongResponse> getAll() throws IOException {
        List<SongSearchEntity> listSong = songRepository.getAll();
        if (listSong.isEmpty()) {
            ListSongResponse response = ListSongResponse.builder()
                    .status("204")
                    .message("Data not available!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        ListSongResponse response = ListSongResponse.builder()
                .status("200")
                .message("Successfully retrieved data!")
                .data(listSong)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @Override
    public ResponseEntity<CreateSongResponse> uploadSong(MultipartFile zipFile, SongEntity entity) throws Exception {
        if(!zipFile.isEmpty()){
            if(isZipFileByMagicNumber(zipFile)) {
                List<File> files = unzipFile(zipFile);
                SongEntity song = readMp3File(files);
                List<Lyrics> listLyrics = new ArrayList<>();
                Map<String, String> lyrics = readLrcFile(files);
                lyrics.forEach((key, value)->listLyrics.add(new Lyrics(key, value)));
                Optional<File> mp3FileOptional = files.stream()
                        .filter(file -> file.getName().endsWith(".mp3")) // Select file Mp3 only
                        .findFirst(); // get first file Mp3

                if (mp3FileOptional.isPresent()) {
                    File mp3File = mp3FileOptional.get();
                    byte[] mp3Bytes = Files.readAllBytes(mp3File.toPath());
                    String url = uploadMp3ToCloudinary(song.getSongName(), mp3Bytes);
                    song.setAudio(url);
                    songRepository.insertSong(song);
                    songRepository.insertLyricsBatch(song.getId(),listLyrics);
                    CreateSongResponse response = CreateSongResponse.builder()
                            .status("200")
                            .message("Creating successful song!")
                            .songUrl(url)
                            .build();
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                CreateSongResponse response = CreateSongResponse.builder()
                        .status("400")
                        .message("Mp3 file not found!")
                        .build();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            CreateSongResponse response = CreateSongResponse.builder()
                    .status("400")
                    .message("Zip file not found!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        CreateSongResponse response = CreateSongResponse.builder()
                .status("400")
                .message("Directory not found")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<UpdateSongResponse> updateSong(int id, SongEntity entity) throws Exception{
        Optional<Song> song = songRepository.getSongById(id);
        if (song.isPresent()) {
            Optional<Song> songByName = songRepository.getSongByName(entity.getSongName());
            if (songByName.isEmpty()) {
                song.get().setSongName(entity.getSongName());
                song.get().setGenreId(entity.getGenreId());
                song.get().setArtistId(entity.getArtistId());
                songRepository.updateSongById(song.get());
                UpdateSongResponse response = UpdateSongResponse.builder()
                        .status("200")
                        .message("Update successfully!")
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            UpdateSongResponse response = UpdateSongResponse.builder()
                    .status("400")
                    .message("The song name already exist!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        UpdateSongResponse response = UpdateSongResponse.builder()
                .status("400")
                .message("The song does not exist!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<DeleteSongResponse> deleteSong(ListSongId listId) {
        List<Integer> listSongByListId = songRepository.getListSongByList(listId);
        if(listId.getListSongId().size()==listSongByListId.size()) {
            songRepository.deleteSongByListId(listId);
            DeleteSongResponse response = DeleteSongResponse.builder()
                    .status("200")
                    .message("Successfully deleted the song!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        DeleteSongResponse response = DeleteSongResponse.builder()
                .status("400")
                .message("Failed to delete the song!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ListSongResponse> searchSongByName(String songName) {
        List<SongSearchEntity> listSong = songRepository.searchSongByName(songName);
        if (listSong.isEmpty()) {
            ListSongResponse response = ListSongResponse.builder()
                    .status("204")
                    .message("No data available!")
                    .data(Collections.emptyList())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ListSongResponse response = ListSongResponse.builder()
                .status("200")
                .message("Get Data successfully!")
                .data(listSong)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListSongResponse> getListSongByUserId(int id) {
        List<SongSearchEntity> listSong = songRepository.getListSongByUserId(id);
        if (listSong.isEmpty()) {
            ListSongResponse response = ListSongResponse.builder()
                    .status("204")
                    .message("No data available!")
                    .data(Collections.emptyList())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ListSongResponse response = ListSongResponse.builder()
                .status("200")
                .message("Get Data successfully!")
                .data(listSong)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private File convertMultiToFile(MultipartFile multipartFile) throws IOException {
        try {
            File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            boolean newFile = convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convFile;
        } catch (IOException e) {
            System.err.print(e);
            return null;
        }
    }


    private boolean isZipFileByMagicNumber(MultipartFile multipartFile) throws IOException {
        try(InputStream inputStream = multipartFile.getInputStream()){
            byte[] signature = new byte[4];
            if(inputStream.read(signature)!=4) {
                return false;
            }
            return  signature[0] == 0x50 && signature[1] == 0x4B
                    && signature[2] == 0x03 && signature[3] == 0x04;
        }
    }

    private List<File> unzipFile(MultipartFile zipFile) throws IOException {
        List<File> extractedFiles = new ArrayList<>();
        File destinationDir = new File(uploadDir);
        if(!destinationDir.exists()) {
            final var mkdir = destinationDir.mkdirs();
        }

        try (InputStream fis = zipFile.getInputStream()){
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null){
                String fileName = entry.getName();
                if(fileName.endsWith(".mp3") || fileName.endsWith(".lrc")){
                    File newFile = new File(uploadDir, fileName);
                    final var createFile = new File(newFile.getParent()).mkdir();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                    extractedFiles.add(newFile);
                }
                zis.closeEntry();
            }
        }
        return extractedFiles;
    }

    private Optional<File[]> safeListFiles(File file){
        return Optional.ofNullable(file.listFiles());
    }

    private List<File> findFilesExtension(List<File> files, String... extensions)throws  {
        List<File> result = new ArrayList<>();

        for(File file: files) {
            if (file.isDirectory()) {
                List<File> foundFiles = findFilesExtension(List.of(file.listFiles()), extensions);
                result.addAll(foundFiles);
            }
            List<File> foundFiles = safeListFiles(file)
                    .map(subFiles -> findFilesExtension(List.of(subFiles), extensions))
                    .orElseThrow(() -> new IllegalStateException("Cannot process directory: " + file.getPath()));
            result.addAll(foundFiles);
        }
        return null;
    }

    private SongEntity readMp3File(List<File> files) throws Exception {
        SongEntity song = SongEntity.builder().build();
        for(File file: files) {
            if (file.getName().endsWith(".mp3")) {
                Mp3File mp3 = new Mp3File(file);
                if (mp3.hasId3v1Tag()) {
                    song.setSongName(mp3.getId3v1Tag().getTitle());
                }
                if (mp3.hasId3v2Tag()) {
                    song.setSongName(mp3.getId3v2Tag().getTitle());
                }
                Integer duration = (int) mp3.getLengthInSeconds();
                song.setDuration(duration);
            }
        }
        return song;
    }

    @NotNull
    private Map<String, String> readLrcFile(List<File> files) {
        Map<String, String> lyrics = new LinkedHashMap<>();
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".lrc")) { // Chỉ xử lý file .lrc
                try (BufferedReader reader  = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.matches("\\[\\d{2}:\\d{2}\\.\\d{2}].*")) {
                            String time = line.substring(1, 8);  // Extract time (mm:ss.xx)
                            String text = line.substring(10);    // Extract lyric text
                            lyrics.put(time, text);
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return lyrics;
    }
    private String uploadMp3ToCloudinary(String publicId, byte[] mp3Bytes)throws IOException {
        String url = "";
        try {
            Map<String, Object> uploadConfig = new HashMap<>();
            uploadConfig.put("resource_type", "auto");
            uploadConfig.put("public_id", publicId);
            uploadConfig.put("eager_async", true);
            Map<?, ?> cloudinaryResponse = this.cloudinary.uploader()
                    .upload(mp3Bytes,uploadConfig);
            url = (String) cloudinaryResponse.get("secure_url");
            return url;
        }catch (IOException e) {
            System.err.print("error:" + e);
        }
        return url;
    }
}
