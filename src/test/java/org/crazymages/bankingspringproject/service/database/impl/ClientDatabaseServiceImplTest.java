package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.client.ClientDto;
import org.crazymages.bankingspringproject.dto.client.mapper.ClientCreationMapper;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.dto.client.mapper.ClientDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientDatabaseServiceImplTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientDtoMapper clientDTOMapper;
    @Mock
    ClientCreationMapper clientCreationMapper;
    @Mock
    EntityUpdateService<Client> clientUpdateService;
    @Mock
    ManagerDatabaseService managerDatabaseService;
    @Mock
    AccountDatabaseService accountDatabaseService;

    @InjectMocks
    ClientDatabaseServiceImpl clientDatabaseService;

    Client client1;
    Client client2;
    ClientDto clientDto1;
    ClientDto clientDto2;
    UUID uuid;
    List<Client> clients;
    List<ClientDto> clientDtoList;

    @BeforeEach
    void setUp() {
        client1 = Client.builder().build();
        client2 = Client.builder().build();
        clientDto1 = ClientDto.builder().build();
        clientDto2 = ClientDto.builder().build();
        uuid = UUID.fromString("d358838e-1134-4101-85ac-5d99e8debfae");
        clients = List.of(client1, client2);
        clientDtoList = List.of(clientDto1, clientDto2);
    }

    @Test
    void create_success_withNullManagerUuid() {
        // given
        List<Manager> managers = List.of(new Manager(), new Manager());
        ManagerStatus status = ManagerStatus.ACTIVE;
        Manager firstManager = new Manager();
        when(clientCreationMapper.mapDtoToEntity(clientDto1)).thenReturn(client1);
        when(managerDatabaseService.findManagersSortedByClientQuantityWhereManagerStatusIs(status))
                .thenReturn(managers);
        when(managerDatabaseService.getFirstManager(managers)).thenReturn(firstManager);

        // when
        clientDatabaseService.create(clientDto1);

        // then
        assertEquals(firstManager.getUuid(), client1.getManagerUuid());
        verify(clientCreationMapper).mapDtoToEntity(clientDto1);
        verify(managerDatabaseService).findManagersSortedByClientQuantityWhereManagerStatusIs(status);
        verify(managerDatabaseService).getFirstManager(managers);
        verify(clientRepository).save(client1);
    }

    @Test
    void create_success_withExistingManagerUuid() {
        // given
        UUID managerUuid = UUID.randomUUID();
        client1.setManagerUuid(managerUuid);
        clientDto1.setManagerUuid(String.valueOf(managerUuid));
        when(clientCreationMapper.mapDtoToEntity(clientDto1)).thenReturn(client1);

        // when
        clientDatabaseService.create(clientDto1);

        // then
        assertEquals(managerUuid, client1.getManagerUuid());
        verify(clientCreationMapper).mapDtoToEntity(clientDto1);
        verify(clientRepository).save(client1);
    }

    @Test
    void findAll_success() {
        // given
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAll();
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        when(clientRepository.findAllNotDeleted()).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAllNotDeleted();
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void findDeletedClients_success() {
        // given
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        when(clientRepository.findAllDeleted()).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findDeletedClients();

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAllDeleted();
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void findById_success() {
        // given
        ClientDto expected = clientDto1;
        when(clientRepository.findById(uuid)).thenReturn(Optional.ofNullable(client1));
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);

        // when
        ClientDto actual = clientDatabaseService.findById(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findById(uuid);
        verify(clientDTOMapper).mapEntityToDto(client1);
    }

    @Test
    void findById_clientNotFound_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(clientRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> clientDatabaseService.findById(strUuid));
        verify(clientRepository).findById(uuid);
    }

    @Test
    void update() {
        // given
        ClientDto updatedClientDto = clientDto1;
        Client updatedClient = client1;
        Client clientToUpdate = client2;

        when(clientCreationMapper.mapDtoToEntity(updatedClientDto)).thenReturn(updatedClient);
        when(clientRepository.findById(uuid)).thenReturn(Optional.ofNullable(clientToUpdate));
        when(clientUpdateService.update(clientToUpdate, updatedClient)).thenReturn(client1);


        // when
        clientDatabaseService.update(String.valueOf(uuid), updatedClientDto);


        // then
        verify(clientCreationMapper).mapDtoToEntity(updatedClientDto);
        verify(clientRepository).findById(uuid);
        verify(clientUpdateService).update(clientToUpdate, updatedClient);
        verify(clientRepository).save(client1);
    }

    @Test
    void update_clientNotFound_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        ClientDto updatedClientDto = clientDto1;
        when(clientRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> clientDatabaseService.update(strUuid, updatedClientDto));
        verify(clientRepository).findById(uuid);
    }

    @Test
    void delete_success() {
        // given
        when(clientRepository.findById(uuid)).thenReturn(Optional.ofNullable(client1));

        // when
        clientDatabaseService.delete(String.valueOf(uuid));

        // then
        assertTrue(client1.isDeleted());
        verify(clientRepository).findById(uuid);
        verify(clientRepository).save(client1);
    }

    @Test
    void delete_clientNotFound_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(clientRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> clientDatabaseService.delete(strUuid));
        verify(clientRepository).findById(uuid);
    }

    @Test
    void findActiveClients_success() {
        // given
        ClientStatus status = ClientStatus.ACTIVE;
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        when(clientRepository.findClientsByStatusIs(status)).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findActiveClients();

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findClientsByStatusIs(status);
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void findClientsWhereBalanceMoreThan_success() {
        // given
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        BigDecimal balance = BigDecimal.valueOf(100);
        when(clientRepository.findAllClientsWhereBalanceMoreThan(balance)).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findClientsWhereBalanceMoreThan(balance);

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAllClientsWhereBalanceMoreThan(balance);
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void findClientsWhereTransactionMoreThan_success() {
        // given
        List<ClientDto> expected = List.of(clientDto1, clientDto2);
        Integer count = 10;
        when(clientRepository.findAllClientsWhereTransactionMoreThan(count)).thenReturn(clients);
        when(clientDTOMapper.mapEntityToDto(client1)).thenReturn(clientDto1);
        when(clientDTOMapper.mapEntityToDto(client2)).thenReturn(clientDto2);

        // when
        List<ClientDto> actual = clientDatabaseService.findClientsWhereTransactionMoreThan(count);

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAllClientsWhereTransactionMoreThan(count);
        verify(clientDTOMapper, times(2)).mapEntityToDto(any(Client.class));
    }

    @Test
    void calculateTotalBalanceByClientUuid_withPositiveBalance_success() {
        // given
        BigDecimal expected = BigDecimal.valueOf(100);
        when(clientRepository.calculateTotalBalanceByClientUuid(uuid)).thenReturn(BigDecimal.valueOf(100));

        // when
        BigDecimal actual = clientDatabaseService.calculateTotalBalanceByClientUuid(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(clientRepository).calculateTotalBalanceByClientUuid(uuid);
    }

    @Test
    void calculateTotalBalanceByClientUuid_withZeroBalance_success() {
        // given
        BigDecimal expected = BigDecimal.ZERO;
        when(clientRepository.calculateTotalBalanceByClientUuid(uuid)).thenReturn(expected);

        // when
        BigDecimal actual = clientDatabaseService.calculateTotalBalanceByClientUuid(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(clientRepository).calculateTotalBalanceByClientUuid(uuid);
    }

    @Test
    void isClientStatusActive_clientIsActive_success() {
        // given
        Boolean expected = true;
        when(clientRepository.isClientStatusBlocked(uuid)).thenReturn(true);

        // when
        Boolean actual = clientDatabaseService.isClientStatusBlocked(uuid);

        // then
        assertEquals(expected, actual);
        verify(clientRepository).isClientStatusBlocked(uuid);
    }

    @Test
    void isClientStatusActive_clientStatusBlocked_success() {
        // given
        Boolean expected = false;
        when(clientRepository.isClientStatusBlocked(uuid)).thenReturn(false);

        // when
        Boolean actual = clientDatabaseService.isClientStatusBlocked(uuid);

        // then
        assertEquals(expected, actual);
        verify(clientRepository).isClientStatusBlocked(uuid);
    }

    @Test
    void findClientsWithCurrentAndSavingsAccounts_success() {
        // given
        AccountType current = AccountType.CURRENT;
        AccountType savings = AccountType.SAVINGS;
        List<Client> expected = List.of(client1, client2);
        when(clientRepository.findAllActiveClientsWithTwoDifferentAccountTypes(current, savings)).thenReturn(clients);

        // when
        List<Client> actual = clientDatabaseService.findClientsWithCurrentAndSavingsAccounts();

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findAllActiveClientsWithTwoDifferentAccountTypes(current, savings);
    }

    @Test
    void findClientsByStatus_success() {
        // given
        List<Client> expected = List.of(client1, client2);
        ClientStatus status = ClientStatus.BLOCKED;
        when(clientRepository.findClientsByStatusIs(status)).thenReturn(clients);

        // when
        List<Client> actual = clientDatabaseService.findClientsByStatus(status);

        // then
        assertEquals(expected, actual);
        verify(clientRepository).findClientsByStatusIs(status);
    }

    @Test
    void blockClientById_success() {
        // when
        clientDatabaseService.blockClientById(String.valueOf(uuid));

        // then
        verify(clientRepository).blockClientById(uuid);
        verify(accountDatabaseService).blockAccountsByClientUuid(String.valueOf(uuid));
    }
}
