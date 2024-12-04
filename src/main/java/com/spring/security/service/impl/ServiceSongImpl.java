package com.spring.security.service.impl;

import com.cloudinary.Cloudinary;
import com.spring.security.entity.SongEntity;
import com.spring.security.entity.SongSearchEntity;
import com.spring.security.model.Song;
import com.spring.security.repository.SongRepository;
import com.spring.security.request.ListSongId;
import com.spring.security.response.song.CreateSongResponse;
import com.spring.security.response.song.DeleteSongResponse;
import com.spring.security.response.song.ListSongResponse;
import com.spring.security.response.song.UpdateSongResponse;
import com.spring.security.service.ServiceSong;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.zip.ZipFile;

@SuppressWarnings({"CallToPrintStackTrace", "ThrowablePrintedToSystemOut"})
@Service
public class ServiceSongImpl implements ServiceSong {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Value("${folder.specified.name}")
    private String uploadUrl;

    @Value("${upload.dir}")
    private String uploadDir;

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
    public ResponseEntity<CreateSongResponse> uploadSong(MultipartFile multipartFile, SongEntity entity) throws Exception {
        if (!multipartFile.isEmpty()) {
            //upload file with name song is null
            String fileName = multipartFile.getOriginalFilename();
            var publicId = uploadUrl.concat(fileName.substring(0, fileName.indexOf(".")));
            Map<String, Object> uploadConfig = new HashMap<>();
            uploadConfig.put("resource_type", "auto");
            uploadConfig.put("public_id", publicId);
            uploadConfig.put("eager_async", true);
//            entity.setDuration(duration);
//            entity.setLyrics(lyrics);
            if (entity.getSongName() != null) {
                Map<?, ?> cloudinaryResponse = this.cloudinary.uploader()
                        .upload(multipartFile.getBytes(),uploadConfig);
                String url = (String) cloudinaryResponse.get("secure_url");
                entity.setAudio(url);
                songRepository.insertSong(entity);
                CreateSongResponse response = CreateSongResponse.builder()
                        .status("200")
                        .message("Creating successful song!")
                        .songUrl(url)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            Map<?, ?> cloudinaryResponse = this.cloudinary.uploader()
                    .upload(multipartFile.getBytes(),uploadConfig);
            String url = (String) cloudinaryResponse.get("secure_url");
            entity.setSongName(fileName);
            entity.setAudio(url);
            songRepository.insertSong(entity);
            CreateSongResponse response = CreateSongResponse.builder()
                    .status("200")
                    .message("Creating successful song!")
                    .songUrl(url)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    private void unzipFile(MultipartFile zipFile) throws IOException {
        File tempZipFile = new File(uploadDir, Objects.requireNonNull(zipFile.getOriginalFilename()));
        zipFile.transferTo(tempZipFile);

        try(ZipFile zip = new ZipFile(tempZipFile)){
            zip.stream().forEach(entry -> {
                try (InputStream zipInputStream = zip.getInputStream(entry)) {
                    File outputFile = new File(uploadDir, entry.getName());
                    if(entry.isDirectory()){
                        boolean mkdir = outputFile.mkdirs();
                    } else {
                        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zipInputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Handle exception appropriately
                }
            });
        }catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
}
