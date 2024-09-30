package com.onlydive.onlydive.controller;

import com.onlydive.onlydive.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@AllArgsConstructor
@Slf4j
public class ImageController{

    private final ImageService imageService;

//    @PostMapping("/upload")
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
//        Image image = imageService.saveImage(file);
//        return "Image uploaded successfully: " + image.getId();
//    }
//
//    // Endpoint do pobierania obrazu po ID
//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws SQLException {
//        Optional<Image> imageOptional = imageService.getImage(id);
//
//        if (imageOptional.isPresent()) {
//            Image image = imageOptional.get();
//            Blob blob = image.getData();
//            byte[] data = blob.getBytes(1, (int) blob.length());  // Konwersja Blob na byte[]
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
//                    .contentType(MediaType.IMAGE_JPEG)  // Zakładamy, że obraz to JPEG
//                    .body(data);
//        }
//
//        return ResponseEntity.notFound().build();
//    }
//}
}
