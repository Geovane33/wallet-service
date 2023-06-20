package com.purebank.walletservice.wallet.reponsitory;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import com.purebank.walletservice.wallet.repository.WalletActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class WalletActivityRepositoryTest {

    @Autowired
    private WalletActivityRepository walletActivityRepository;

    @Test
    @DisplayName("Buscar atividade por UUID existente")
    public void findByUuidActivity_ExistingUuidActivity_ReturnsWalletActivity() {
        String uuidActivity = "cda49476-2bab-4bcf-8160-c3fc907e4f58";
        WalletActivity expectedActivity = createWalletActivity(uuidActivity);
        walletActivityRepository.save(expectedActivity);
        Optional<WalletActivity> result = walletActivityRepository.findByUuidActivity(uuidActivity);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedActivity, result.get());
    }

    @Test
    @DisplayName("Buscar atividade por UUID não existente")
    public void findByUuidActivity_NonExistingUuidActivity_ReturnsEmptyOptional() {
        String uuidActivity = "non-existing-uuid";

        Optional<WalletActivity> result = walletActivityRepository.findByUuidActivity(uuidActivity);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Buscar atividades da carteira por ID ordenadas pela data de criação ascendente")
    public void findByWalletIdOrderByCreationDateAsc_ExistingWalletId_ReturnsSortedWalletActivities() {
        Long walletId = 1L;
        WalletActivity activity1 = createWalletActivity("uuid1");
        activity1.setCreationDate(LocalDateTime.now().minusDays(1));
        WalletActivity activity2 = createWalletActivity("uuid2");
        activity2.setCreationDate(LocalDateTime.now().minusDays(2));
        WalletActivity activity3 = createWalletActivity("uuid3");
        activity3.setCreationDate(LocalDateTime.now().minusDays(3));

        walletActivityRepository.save(activity3);
        walletActivityRepository.save(activity2);
        walletActivityRepository.save(activity1);

        Optional<List<WalletActivity>> result = walletActivityRepository.findByWalletIdOrderByCreationDateDesc(walletId);

        Assertions.assertTrue(result.isPresent());
        List<WalletActivity> activities = result.get();
        Assertions.assertEquals(3, activities.size());
        Assertions.assertEquals(activity1, activities.get(0));
        Assertions.assertEquals(activity2, activities.get(1));
        Assertions.assertEquals(activity3, activities.get(2));
    }

    @Test
    @DisplayName("Buscar atividades da carteira por ID inexistente")
    public void findByWalletIdOrderByCreationDateAsc_NonExistingWalletId_ReturnsEmptyOptional() {
        Long walletId = 999L;

        Optional<List<WalletActivity>> result = walletActivityRepository.findByWalletIdOrderByCreationDateDesc(walletId);

        Assertions.assertTrue(result.get().isEmpty());
    }

    private WalletActivity createWalletActivity(String uuidActivity) {
        WalletActivity activity = new WalletActivity();
        activity.setWalletId(1L);
        activity.setUuidActivity(uuidActivity);
        activity.setActivityType(ActivityType.DEPOSIT);
        activity.setStatus(ProcessStatus.COMPLETED);
        activity.setDescription("Tranferencia realizada");
        activity.setAmount(BigDecimal.valueOf(100));
        activity.setActivityDate(LocalDateTime.now());
        activity.setCreationDate(LocalDateTime.now());
        activity.setLastUpdate(LocalDateTime.now());
        return activity;
    }
}


