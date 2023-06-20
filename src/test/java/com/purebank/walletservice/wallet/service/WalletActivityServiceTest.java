package com.purebank.walletservice.wallet.service;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import com.purebank.walletservice.wallet.repository.WalletActivityRepository;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.impl.WalletActivityServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {WalletActivityService.class, WalletActivityServiceImpl.class})
public class WalletActivityServiceTest {
    @Autowired
    private WalletActivityService walletActivityService;

    @MockBean
    private WalletActivityRepository walletActivityRepository;

    @Test
    @DisplayName("Salvar WalletActivity com sucesso")
    public void testSaveWalletActivity() {
        WalletActivityResource walletActivityResource = new WalletActivityResource();

        WalletActivity walletActivity = new WalletActivity();

        Mockito.when(walletActivityRepository.findByUuidActivity(walletActivityResource.getUuidActivity()))
                .thenReturn(Optional.of(walletActivity));
        Mockito.when(walletActivityRepository.save(Mockito.any(WalletActivity.class))).thenReturn(walletActivity);
        walletActivityResource.setStatus(ProcessStatus.COMPLETED);
        walletActivityService.createOrUpdate(walletActivityResource);

        Mockito.verify(walletActivityRepository, Mockito.times(1)).save(Mockito.any(WalletActivity.class));
    }

    @Test
    @DisplayName("Falha ao salvar WalletActivity")
    public void testSaveWalletActivityThrowsException() {
        WalletActivityResource walletActivityResource = new WalletActivityResource();

        Mockito.when(walletActivityRepository.findByUuidActivity(walletActivityResource.getUuidActivity()))
                .thenReturn(Optional.empty());

        Mockito.when(walletActivityRepository.save(Mockito.any(WalletActivity.class)))
                .thenThrow(new RuntimeException("Mock RuntimeException"));

        Assertions.assertThrows(RuntimeException.class, () -> walletActivityService.createOrUpdate(walletActivityResource));
    }

    @Test
    @DisplayName("Buscar atividades por ID da carteira - Resultado vazio")
    public void findByWalletIdOrderByCreationDateAscResultEmptyTest() {
        WalletActivityRepository walletActivityRepository = Mockito.mock(WalletActivityRepository.class);
        List<WalletActivity> mockActivities = new ArrayList<>();

        Mockito.when(walletActivityRepository.findByWalletIdOrderByCreationDateDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(mockActivities));

        List<WalletActivityResource> result = walletActivityService.activity(123L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockActivities.size(), result.size());
    }

    @Test
    @DisplayName("Buscar atividades por ID da carteira - Sucesso")
    public void findByWalletIdOrderByCreationDateAscTest() {
        List<WalletActivity> mockActivities = createWalletActivityList();

        Mockito.when(walletActivityRepository.findByWalletIdOrderByCreationDateDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(mockActivities));

        List<WalletActivityResource> result = walletActivityService.activity(123L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockActivities.size(), result.size());

        for (int i = 0; i < mockActivities.size(); i++) {
            WalletActivity expectedActivity = mockActivities.get(i);
            WalletActivityResource actualResource = result.get(i);

            Assertions.assertEquals(expectedActivity.getUuidActivity(), actualResource.getUuidActivity());
            Assertions.assertEquals(expectedActivity.getWalletId(), actualResource.getWalletId());
            Assertions.assertEquals(expectedActivity.getActivityType(), actualResource.getActivityType());
            Assertions.assertEquals(expectedActivity.getStatus(), actualResource.getStatus());
            Assertions.assertEquals(expectedActivity.getAmount(), actualResource.getAmount());
        }
    }

    public static List<WalletActivity> createWalletActivityList() {
        List<WalletActivity> walletActivities = new ArrayList<>();
        WalletActivity activity = new WalletActivity();
        activity.setId(1L);
        activity.setWalletId(123L);
        activity.setUuidActivity("uuid1");
        activity.setActivityType(ActivityType.DEPOSIT);
        activity.setStatus(ProcessStatus.COMPLETED);
        activity.setAmount(BigDecimal.valueOf(100.00));
        activity.setActivityDate(LocalDateTime.now());
        activity.setCreationDate(LocalDateTime.now());
        activity.setLastUpdate(LocalDateTime.now());
        walletActivities.add(activity);

        WalletActivity activity2 = new WalletActivity();
        activity2.setId(2L);
        activity2.setWalletId(123L);
        activity2.setUuidActivity("uuid2");
        activity2.setActivityType(ActivityType.WITHDRAW);
        activity2.setStatus(ProcessStatus.FAILED);
        activity2.setAmount(BigDecimal.valueOf(50.00));
        activity2.setActivityDate(LocalDateTime.now());
        activity2.setCreationDate(LocalDateTime.now());
        activity2.setLastUpdate(LocalDateTime.now());
        walletActivities.add(activity2);
        return walletActivities;
    }

}
