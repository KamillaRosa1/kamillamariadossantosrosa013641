package com.projeto.api_artistas.service;

import com.projeto.api_artistas.dto.AlbumDTO;
import com.projeto.api_artistas.model.Album;
import com.projeto.api_artistas.model.Artista;
import com.projeto.api_artistas.repository.AlbumRepository;
import com.projeto.api_artistas.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final S3Service s3Service;

    public AlbumService(AlbumRepository albumRepository, 
                        ArtistaRepository artistaRepository, 
                        SimpMessagingTemplate messagingTemplate,
                        S3Service s3Service) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.messagingTemplate = messagingTemplate;
        this.s3Service = s3Service;
    }

    public Page<Album> buscarAlbuns(String nomeArtista, String tipo, Pageable pageable) {
        if (nomeArtista != null && !nomeArtista.isEmpty()) {
            return albumRepository.findByArtistas_NomeContainingIgnoreCase(nomeArtista, pageable);
        }
        if (tipo != null && !tipo.isEmpty()) {
            return albumRepository.findByArtistas_Tipo(tipo, pageable);
        }
        return albumRepository.findAll(pageable);
    }

    /**
     * Requisito 'g', 'h' e 'i': Upload de imagem e retorno de link pré-assinado.
     */
    @Transactional
    public String uploadCapa(Long id, MultipartFile arquivo) throws IOException {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        // Utiliza o seu S3Service original
        String fileName = s3Service.uploadFile(arquivo);

        album.setImagemCapaUrl(fileName);
        albumRepository.save(album);

        // Retorna o link de 30 minutos conforme requisito 'i'
        return s3Service.gerarLinkPreAssinado(fileName);
    }

    /**
     * Requisito 'c' e 'Sênior c': Salvar dados e notificar via WebSocket.
     */
    @Transactional
    public Album salvar(AlbumDTO dto) {
        Album album = (dto.getId() != null) 
            ? albumRepository.findById(dto.getId()).orElse(new Album()) 
            : new Album();
        
        album.setTitulo(dto.getTitulo());

        if (dto.getArtistas() != null && !dto.getArtistas().isEmpty()) {
            List<Artista> artistasPersistidos = artistaRepository.findAllById(dto.getArtistas());
            album.getArtistas().clear();
            album.getArtistas().addAll(artistasPersistidos);
        }

        Album salvo = albumRepository.save(album);

        // Notificação WebSocket
        messagingTemplate.convertAndSend("/topic/albuns", "Novo álbum cadastrado: " + salvo.getTitulo());

        return salvo;
    }
}