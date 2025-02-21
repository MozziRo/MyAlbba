package com.ssafy.myalbba.salary.service;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.entity.Worker;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.Member.repository.WorkerRepository;
import com.ssafy.myalbba.Member.service.EmailSenderService;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import com.ssafy.myalbba.memo.entity.Status;
import com.ssafy.myalbba.salary.dto.DailySalaryDTO;
import com.ssafy.myalbba.salary.dto.SalaryBankDTO;
import com.ssafy.myalbba.salary.dto.SalaryDTO;
import com.ssafy.myalbba.salary.entity.DailySalary;
import com.ssafy.myalbba.salary.entity.Salary;
import com.ssafy.myalbba.salary.repository.DailySalaryRepository;
import com.ssafy.myalbba.salary.repository.SalaryRepository;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleRequestDTO;
import com.ssafy.myalbba.schedule.dto.ConfirmedScheduleResponseDTO;
import com.ssafy.myalbba.schedule.entity.ConfirmedSchedule;
import com.ssafy.myalbba.schedule.repository.ConfirmedScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class SalarySerivceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final DailySalaryRepository dailySalaryRepository;
    private final WorkerRepository workerRepository;
    private final StoreRepository storeRepository;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;
    private final ConfirmedScheduleRepository confirmedScheduleRepository;

    @Override
    public void registerSalary(String loginUserEmail, List<ConfirmedScheduleResponseDTO> confirmedScheduleList) {
        Store store = userRepository.findByEmail(loginUserEmail).getStore();

        List<DailySalary> dailySalaries = confirmedScheduleList.stream()
                .map(dto -> {
                    Double hours = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes() / 60.0;

                    Worker worker = workerRepository.findById(dto.getWorkerId())
                            .orElseThrow(() -> new RuntimeException("해당 알바생이 없습니다."));

                    Double beforeTax = hours * worker.getHourlySalary();
                    int afterTax = (int)(beforeTax - beforeTax * worker.getTax() - beforeTax * worker.getInsurance());

                    DailySalary dailySalary = DailySalary.createDailySalary(dto.getConfirmedScheduleId(), dto.getDate(), hours, afterTax);
                    dailySalary.setWorker(worker);
                    dailySalary.setStore(store);

            return dailySalary;
        })
        .collect(Collectors.toList());

        List<DailySalary> savedDailySalaries = dailySalaryRepository.saveAll(dailySalaries);

        // 알바생별로 그룹화하고 월급 합산하여 저장
        Map<Worker, double[]> monthlySalaryMap = savedDailySalaries.stream()
                .collect(Collectors.groupingBy(
                        DailySalary::getWorker,
                        Collectors.reducing(
                                new double[]{0.0, 0}, // {totalHours, monthlySalary}
                                dailySalary -> new double[]{dailySalary.getHours(), dailySalary.getDailyAmount()},
                                (a, b) -> new double[]{a[0] + b[0], a[1] + b[1]} // a : 현재까지 합산된 값, b : 새로 추가될 데이터의 값
                        )
                ));
        
        List<Salary> salaries = monthlySalaryMap.entrySet().stream()
                .map(entry -> {
                    Worker worker = entry.getKey();
                    double[] totals = entry.getValue();
                    double totalHours = totals[0];
                    int monthlySalary = (int) totals[1];
                    
                    LocalDate salaryDate = confirmedScheduleList.get(0).getDate().withDayOfMonth(1); // yyyy-mm-01로 설정
                    Salary salary = salaryRepository.findByWorkerIdAndSalaryDate(worker.getId(), salaryDate);

                    if(salary == null) {
                        salary = Salary.createSalary(salaryDate, totalHours, monthlySalary);
                        salary.setWorker(worker);
                        salary.setStore(store);
                    } else { // 같은 workerIdAndSalaryDate가 2개 이상 생기지 않도록
                        int updateMonthlySalary = salary.getMonthlySalary() + monthlySalary;
                        Double updateTotalHours = salary.getTotalHours() + totalHours;
                        salary.updateMonthlySalary(updateMonthlySalary, updateTotalHours);
                    }

                    return salary;
                }).collect(Collectors.toList());

        salaryRepository.saveAll(salaries);
    }

    @Override
    public void addNewSalary(String loginUserEmail, ConfirmedScheduleResponseDTO confirmedSchedule) {
        Store store = userRepository.findByEmail(loginUserEmail).getStore();

        Double hours = Duration.between(confirmedSchedule.getStartTime(), confirmedSchedule.getEndTime()).toMinutes() / 60.0;
        Worker worker = workerRepository.findById(confirmedSchedule.getWorkerId())
                .orElseThrow(() -> new RuntimeException("해당 알바생이 없습니다."));

        Double beforeTax = hours * worker.getHourlySalary();
        int afterTax = (int)(beforeTax - beforeTax * worker.getTax() - beforeTax * worker.getInsurance());

        DailySalary dailySalary = DailySalary.createDailySalary(confirmedSchedule.getConfirmedScheduleId(), confirmedSchedule.getDate(), hours, afterTax);
        dailySalary.setWorker(worker);
        dailySalary.setStore(store);

        dailySalaryRepository.save(dailySalary);

        LocalDate salaryDate = dailySalary.getDate().withDayOfMonth(1);
        Salary salary = salaryRepository.findByWorkerIdAndSalaryDate(worker.getId(), salaryDate);

        if(salary != null) {
            int updateMonthlySalary = salary.getMonthlySalary() + dailySalary.getDailyAmount();
            Double updateTotalHours = salary.getTotalHours() + dailySalary.getHours();
            salary.updateMonthlySalary(updateMonthlySalary, updateTotalHours);
        } else {
            salary = Salary.createSalary(salaryDate, hours, afterTax);
            salary.setWorker(worker);
            salary.setStore(store);
        }
        salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public void deleteSalary(Long confirmedScheduleId) {
        ConfirmedSchedule confirmedSchedule = confirmedScheduleRepository.findById(confirmedScheduleId)
                .orElseThrow(() -> new RuntimeException("해당 스케줄이 없습니다."));

        Long workerId = confirmedSchedule.getWorker().getId();
        
        // 해당 스케줄에 해당하는 일급 삭제
        DailySalary dailySalary = dailySalaryRepository.findByConfirmedScheduleId(confirmedScheduleId);
        dailySalaryRepository.delete(dailySalary);

        // 해당 스케줄에 해당하는 월급에서 일급 제외
        LocalDate salaryDate = dailySalary.getDate().withDayOfMonth(1);
        Salary salary = salaryRepository.findByWorkerIdAndSalaryDate(workerId, salaryDate);

        int updateMonthlySalary = salary.getMonthlySalary() - dailySalary.getDailyAmount();
        Double updateTotalHours = salary.getTotalHours() - dailySalary.getHours();
        salary.updateMonthlySalary(updateMonthlySalary, updateTotalHours);
        salaryRepository.save(salary);
    }

    @Override
    public List<DailySalaryDTO> getAllDailySalary(String loginUserEmail) {
        Long storeId = userRepository.findByEmail(loginUserEmail).getStore().getId();

        return dailySalaryRepository.findByStoreId(storeId).stream()
                .map(DailySalaryDTO::from)
                .toList();
    }

    @Override
    public List<SalaryDTO> getAllMonthlySalary(String loginUserEmail) {
        Long storeId = userRepository.findByEmail(loginUserEmail).getStore().getId();

        return salaryRepository.findByStoreId(storeId).stream()
                .map(SalaryDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailySalaryDTO> getMyDailySalary(String loginUserEmail) {
        Long workerId = userRepository.findByEmail(loginUserEmail).getId();

        return dailySalaryRepository.findByWorkerId(workerId).stream()
                .map(DailySalaryDTO::from)
                .toList();
    }

    @Override
    public List<SalaryDTO> getMyMonthlySalary(String loginUserEmail) {
        Long workerId = userRepository.findByEmail(loginUserEmail).getId();

        return salaryRepository.findByWorkerId(workerId).stream()
                .map(SalaryDTO::from)
                .toList();
    }

    @Override
    public void updateSalaryStatusAsCompleted(Long salaryId) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new RuntimeException("해당 월급이 없습니다."));

        salary.updateSalaryStatus();
        emailSenderService.sendEmailPayCheck(salary.getWorker().getEmail());
        salaryRepository.save(salary);
    }

    @Override
    @Scheduled(cron = "0 0 9,12,15,18 * * ?")
    public void sendPaydayAlarm() {
        // 오늘이 월급날인 가게들 가져오기
        int today = LocalDate.now().getDayOfMonth();
        List<Store> stores = storeRepository.findByPayday(today).stream().toList();

        for (Store store : stores) {
            List<Salary> salaries = salaryRepository.findByStoreId(store.getId());

            for(Salary salary : salaries) {
                if(salary.getStatus().equals(Status.NOT_DONE)) { // 월급 정산이 안된 알바생이 있다면 메일 보내기
                    String ownerEmail = store.getOwner().getEmail();
                    emailSenderService.sendEmailPaydayAlarm(ownerEmail);
                    break;
                }
            }
        }
    }

    @Override
    public SalaryBankDTO getMyBankName(String userEmail) {
        UserEntity userEntity = userRepository.findByEmail(userEmail);

        Owner owner = (Owner) userEntity;
        return new SalaryBankDTO(owner.getBank());
    }

}
