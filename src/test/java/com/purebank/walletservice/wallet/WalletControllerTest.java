package com.purebank.walletservice.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import com.purebank.walletservice.wallet.service.WalletService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WalletController.class)
public class WalletControllerTest {
    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String API_WALLET = "/api/wallet";

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletActivityService walletActivityService;
    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    @DisplayName("Criar Carteira")
    void createWalletTest() throws Exception {
        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        Mockito.doReturn(walletResource).when(walletService).createWallet(walletResource);

        String requestBody = "{ \"id\": 1, \"name\": \"Test\", \"balance\": 10 }";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(API_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

        Assertions.assertEquals(walletResource, responseWallet);
    }

    @Test
    @DisplayName("Obter Carteira por ID")
    void getWalletByIdTest() throws Exception {
        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        Mockito.doReturn(walletResource).when(walletService).getWalletById(walletResource.getId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(API_WALLET + "/" + walletResource.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

        Assertions.assertEquals(walletResource, responseWallet);
    }

    @Test
    @DisplayName("Obter Saldo")
    void getBalanceTest() throws Exception {
        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        Mockito.doReturn(walletResource.getBalance()).when(walletService).getBalance(walletResource.getId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(API_WALLET + "/" + walletResource.getId() + "/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        BigDecimal responseBalance = objectMapper.readValue(responseBody, BigDecimal.class);

        Assertions.assertEquals(responseBalance, walletResource.getBalance());
    }

    @Test
    @DisplayName("Atualizar Carteira")
    void updateWalletTest() throws Exception {
        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        String requestBody = "{ \"id\": 1, \"name\": \"Test\", \"balance\": 10 }";
        WalletResource walletResourceUpdate = objectMapper.readValue(requestBody, WalletResource.class);
        walletResourceUpdate.setName("Test update");

        Mockito.doReturn(walletResourceUpdate).when(walletService).updateWallet(walletResource);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(API_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

        Assertions.assertEquals(walletResourceUpdate, responseWallet);
    }

    @Test
    @DisplayName("Depositar na Carteira")
    void depositTest() throws Exception {
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(10);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(API_WALLET +  "/{walletId}/deposit", walletId)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Depositar na Carteira - Valor igual a  R$0,00")
    void depositZeroTest() throws Exception {
        long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(0);

        Assertions.assertThrows(ServletException.class,
                () -> mvc.perform(MockMvcRequestBuilders.patch(API_WALLET +  "/{walletId}/deposit", walletId)
                                .param("amount", amount.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()));
    }

    @Test
    @DisplayName("Depositar na Carteira - Valor igual a R$1.000.000,00")
    void depositMaxValueTest() throws Exception {
        long walletId = 1L;
        BigDecimal amount = new BigDecimal("1000000.00");

        Assertions.assertThrows(ServletException.class,
                () -> mvc.perform(MockMvcRequestBuilders.patch(API_WALLET + "/{walletId}/deposit", walletId)
                                .param("amount", amount.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()));
    }

    @Test
    @DisplayName("Saque com Sucesso")
    void withdrawSuccessTest() throws Exception {
        Long walletId = 1L;
        BigDecimal withdrawalAmount = BigDecimal.valueOf(50);

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(100));

        Mockito.when(walletService.withdraw(walletId, withdrawalAmount)).thenReturn(Boolean.TRUE);

        String requestBody = "{ \"walletId\": 1, \"amount\": 50 }";

        mvc.perform(MockMvcRequestBuilders.patch(API_WALLET + "/{walletId}/withdraw", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", withdrawalAmount.toString())
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Saque com Valor igual a R$1.000.000,00")
    void withdrawMaxAmountTest() throws Exception {
        Long walletId = 1L;
        BigDecimal withdrawalAmount = new BigDecimal("1000000.00");

        String requestBody = "{ \"walletId\": 1, \"amount\": 1000000.00 }";

        Assertions.assertThrows(ServletException.class, () -> mvc
                .perform(MockMvcRequestBuilders.patch(API_WALLET + "/{walletId}/withdraw", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", withdrawalAmount.toString())
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn());
    }

    @Test
    @DisplayName("Saque com Valor igual a R$0")
    void withdrawZeroAmountTest() throws Exception {
        Long walletId = 1L;
        BigDecimal withdrawalAmount = BigDecimal.ZERO;

        String requestBody = "{ \"walletId\": 1, \"amount\": 0 }";

        Assertions.assertThrows(ServletException.class, () -> mvc
                .perform(MockMvcRequestBuilders.patch(API_WALLET + "/{walletId}/withdraw", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", withdrawalAmount.toString())
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn());
    }

    @Test
    @DisplayName("Obter atividades da carteira")
    void getWalletActivitiesTest() throws Exception {
        Long walletId = 1L;

        mvc.perform(MockMvcRequestBuilders.get(API_WALLET + "/{walletId}/activities", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
