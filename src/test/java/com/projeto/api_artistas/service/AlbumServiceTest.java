package com.projeto.api_artistas.service;

import com.projeto.api_artistas.model.Album;
import com.projeto.api_artistas.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock private AlbumRepository repository;
    @Mock private S3Service s3Service;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @Mock private MultipartFile arquivo;

    @InjectMocks private AlbumService service;

    @Test
    void deveFazerUploadEGerarLinkPreAssinado() throws Exception {
        Album album = new Album();
        album.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(album));
        when(s3Service.uploadFile(any())).thenReturn("foto.jpg");
        when(s3Service.gerarLinkPreAssinado("foto.jpg")).thenReturn("http://link-assinada.com");

        String url = service.uploadCapa(1L, arquivo);

        assertNotNull(url);
        assertEquals("http://link-assinada.com", url);
        verify(repository, times(1)).save(album);
    }
}