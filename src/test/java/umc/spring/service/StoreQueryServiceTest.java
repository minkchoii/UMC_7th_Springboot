package umc.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import umc.spring.domain.Store;
import umc.spring.service.StoreService.StoreQueryService;

import java.util.List;

@SpringBootTest
public class StoreQueryServiceTest {

    @Autowired
    private StoreQueryService storeQueryService;

    @Test
    public void testFindStoresByNameAndScore() {
        // 조건: 이름이 '요아정'이고, 평점이 4.0 이상인 가게를 조회
        List<Store> stores = storeQueryService.findStoresByNameAndScore("요아정", 4.0f);

        // 조회된 결과를 콘솔에 출력
        stores.forEach(System.out::println);
    }
}
