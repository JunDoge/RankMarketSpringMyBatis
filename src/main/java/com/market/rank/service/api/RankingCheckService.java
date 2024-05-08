package com.market.rank.service.api;

import com.market.rank.dto.request.ReqRankingCheckDto;
import com.market.rank.dto.response.ResRankingCheck;
import com.market.rank.mapper.product.ProductProcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RankingCheckService {

    private final ProductProcMapper productProcMapper;

    public ResRankingCheck uploadImage(ReqRankingCheckDto checkDto) throws URISyntaxException, IOException {



        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getMultiValueMapHttpEntity(checkDto.getImages(), headers);

        String ranking = restTemplate.postForObject(
                new URI("http://localhost:5000/rankingCheck"),
                requestEntity,
                String.class);

        System.out.println("여긴 service ranking임 : " + ranking);

        return ResRankingCheck.builder()
                .rank(ranking)
                .price(productProcMapper.catPrice(checkDto.getCatId() + ranking))
                .build();

    }

    private static HttpEntity<MultiValueMap<String, Object>> getMultiValueMapHttpEntity(List<MultipartFile> files, HttpHeaders headers) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile file : files) {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("images", resource);
        }

        return new HttpEntity<>(body, headers);
    }

}
