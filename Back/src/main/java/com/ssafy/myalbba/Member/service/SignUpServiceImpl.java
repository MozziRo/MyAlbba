package com.ssafy.myalbba.Member.service;

import com.ssafy.myalbba.Member.dto.OwnerDTO;
import com.ssafy.myalbba.Member.dto.EmailCheckDTO;
import com.ssafy.myalbba.Member.dto.PasswordCheckRequestDTO;
import com.ssafy.myalbba.Member.dto.UserInfoDTO;
import com.ssafy.myalbba.Member.dto.WorkerDTO;
import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.Member.repository.OwnerRepository;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.Member.repository.WorkerRepository;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final WorkerRepository workerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StoreRepository storeRepository;

    @Override
    public boolean checkDuplication(EmailCheckDTO emailCheckDTO) {
        return userRepository.existsByEmail(emailCheckDTO.getEmail());
    }

    @Override
    public Long signUpOwner(OwnerDTO ownerDTO) {
        String encodedPassword = bCryptPasswordEncoder.encode(ownerDTO.getPassword());

        Owner owner = Owner.createOwner(ownerDTO.getEmail(), encodedPassword, ownerDTO.getName(), ownerDTO.getRole(), ownerDTO.getPhoneNumber(), ownerDTO.getBank());
        return ownerRepository.save(owner).getId();
    }

    @Override
    public void signUpWorker(WorkerDTO workerDTO) {
        String encodedPassword = bCryptPasswordEncoder.encode(workerDTO.getPassword());

        Store store = ownerRepository.findByEmail(workerDTO.getOwnerEmail())
                .orElseThrow(() -> new RuntimeException("사장님을 찾을 수 없습니다.")).getStore();

        Worker worker = Worker.createWorker(workerDTO.getEmail(), encodedPassword, workerDTO.getName(), workerDTO.getRole(), workerDTO.getPhoneNumber(), workerDTO.getBank(), workerDTO.getAccount());
        worker.setStore(store);

        workerRepository.save(worker);
    }

    @Override
    @Transactional
    public void updateWorker(String email, WorkerDTO workerDTO) {
        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("근무 알바생을 찾을 수 없습니다."));
        worker.updateWorker(workerDTO);
    }

    @Override
    @Transactional
    public void updateWorkerSalaryInfo(Long workerId, WorkerDTO workerDTO) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("근무 알바생을 찾을 수 없습니다."));

        worker.updateWorker(workerDTO);
    }

    @Override
    @Transactional
    public void updateOwner(String loginUserEmail, OwnerDTO ownerDTO) {
        Owner owner = ownerRepository.findByEmail(loginUserEmail)
                .orElseThrow(() -> new RuntimeException("사장님을 찾을 수 없습니다."));

        owner.updateOwner(ownerDTO);
    }

    @Override
    public List<WorkerDTO> getAllWorkers(String loginUserEmail) {
        UserEntity userEntity = userRepository.findByEmail(loginUserEmail);

        Long storeId = userEntity.getStore().getId();
        return workerRepository.findByStoreIdAndEmailNot(storeId, loginUserEmail).stream()
                .map(WorkerDTO::buildForAllWorkers)
                .toList();
    }

    @Override
    public void setStore(Long ownerId, Long storeId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("사장님을 찾을 수 없습니다."));
        
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        owner.setStore(store);
        ownerRepository.save(owner);
    }

    @Override
    public WorkerDTO getWorkerInfo(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("해당 알바생을 찾을 수 없습니다."));

        return WorkerDTO.buildForWorkerInfo(worker);
    }

    @Override
    public WorkerDTO getMyWorkerInfo(String loginUserEmail) {
        Worker worker = workerRepository.findByEmail(loginUserEmail)
                .orElseThrow(() -> new RuntimeException("해당 알바생을 찾을 수 없습니다."));

        return WorkerDTO.buildForWorkerInfo(worker);
    }

    @Override
    public OwnerDTO getMyOwnerInfo(String loginUserEmail) {
        Owner owner = ownerRepository.findByEmail(loginUserEmail)
                .orElseThrow(() -> new RuntimeException("해당 사장님을 찾을 수 없습니다."));

        return OwnerDTO.buildForOwnerInfo(owner);
    }

    @Override
    public UserInfoDTO getMyInfo(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail);
        return new UserInfoDTO(user.getName(), user.getRole());
    }

    @Override
    public Boolean checkPassword(String loginUserEmail, PasswordCheckRequestDTO passwordCheckRequestDTO) {
        UserEntity userEntity = userRepository.findByEmail(loginUserEmail);

        return bCryptPasswordEncoder.matches(passwordCheckRequestDTO.password(), userEntity.getPassword());
    }

}
