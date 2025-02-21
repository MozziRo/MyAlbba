//package com.ssafy.myalbba;
//
//
//import com.ssafy.myalbba.Member.entity.Owner;
//import com.ssafy.myalbba.Member.repository.OwnerRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//import java.util.List;
//import java.util.Optional;
//
//
//@SpringBootTest
//public class OwnerRepositoryTest {
//
//    @Autowired
//    OwnerRepository ownerRepository;
//
//    @BeforeEach
//    public void clear(){
//        ownerRepository.deleteAllExcept(1);
//
////        Owner owner1 = new Owner();
////        owner1.setStoreId(1);
////        owner1.setEmail("kimssafy@naver.com");
////        owner1.setName("Kimssafy");
////        owner1.setPassword("1234");
////        owner1.setPhone("01012341234");
//
//        ownerRepository.save(Owner.builder()
//                .name("name")
//                .email("email")
//                .phone("phone")
//                .build());
//
////        ownerRepository.save(owner1);
//
////        Owner owner2 = new Owner();
////        owner2.setStoreId(1);
////        owner2.setEmail("Leessafy@naver.com");
////        owner2.setName("Leessafy");
////        owner2.setPassword("1234");
////        owner2.setPhone("01012341234");
////
////        ownerRepository.save(owner2);
//    }
//
//    @Test
//    public void insertTest(){
//        Owner owner = new Owner();
//        owner.setStoreId(1);
//        owner.setEmail("test@test");
//        owner.setName("test");
//        owner.setPassword("1234");
//        owner.setPhone("01012341234");
//
//        ownerRepository.save(owner);
//
//        List<Owner> owners = ownerRepository.findAll();
//        Assertions.assertEquals(owners.size(), 4);
//    }
//
//    @Test
//    public void findByOwnerIdTest() { // PK로 조회
//        Optional<Owner> optionalOwner = ownerRepository.findByEmail("Leessafy@naver.com");
//        Owner owner = optionalOwner.get();
//        int id = owner.getOwnerId();
//
//        optionalOwner = ownerRepository.findByOwnerId(id);
//        owner = optionalOwner.get();
//        Assertions.assertEquals(owner.getOwnerId(), id);
//    }
//
//    @Test
//    public void findByStoreIdTest() { // storeId로 조회
//        List<Owner> owners = ownerRepository.findByStoreId(1);
//        Assertions.assertEquals(owners.size(), 2);
//    }
//
//    @Test
//    public void findByEmailTest() { // email로 조회
//        Optional<Owner> optionalOwner = ownerRepository.findByEmail("Leessafy@naver.com");
//        Owner owner = optionalOwner.get();
//        Assertions.assertEquals(owner.getEmail(), "Leessafy@naver.com");
//        System.out.println(owner.getOwnerId());
//    }
//
//    @Test
//    public void findByEmailAndPasswordTest() {
//        String email = "kimssafy@naver.com";
//        String password = "1234";
//        Optional<Owner> optionalOwner = ownerRepository.findByEmailAndPassword(email, password);
//        Owner owner = optionalOwner.get();
//        Assertions.assertEquals(owner.getEmail(), "kimssafy@naver.com");
//        Assertions.assertEquals(owner.getPassword(), "1234");
//    }
//
//    @Test
//    public void findByNameTest() { // name으로 조회
//        Optional<Owner> optionalOwner = ownerRepository.findByName("Kimssafy");
//        Owner owner = optionalOwner.get();
//        Assertions.assertEquals(owner.getName(), "Kimssafy");
//    }
//
//    @Test
//    public void updateTest() {
//        Optional<Owner> optionalOwner = ownerRepository.findByName("Kimssafy");
//        Owner owner = optionalOwner.get();
//
//        owner.setName("Parkssafy");
//        ownerRepository.save(owner);
//
//        optionalOwner = ownerRepository.findByName("Parkssafy");
//        owner = optionalOwner.get();
//        Assertions.assertEquals(owner.getEmail(), "kimssafy@naver.com");
//    }
//
//}
