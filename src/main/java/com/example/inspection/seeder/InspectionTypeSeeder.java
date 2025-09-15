// package com.example.inspection.seeder;

// import com.example.inspection.entity.InspectionType;
// import com.example.inspection.repository.InspectionTypeRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;

// import java.util.List;

// @Component
// @Order(2)
// public class InspectionTypeSeeder implements CommandLineRunner {

// @Autowired
// private InspectionTypeRepository inspectionTypeRepository;

// @Override
// public void run(String... args) throws Exception {
// if (inspectionTypeRepository.count() == 0) {
// InspectionType type1 = new InspectionType();
// type1.setInspectionTypeId("01");
// type1.setName("GIÁM ĐỊNH MÁY MÓC, THIẾT BỊ VÀ DÂY CHUYỀN CÔNG NGHỆ ĐÃ QUA
// SỬ DỤNG");
// type1.setNote("Kiểm tra an toàn");

// InspectionType type2 = new InspectionType();
// type2.setInspectionTypeId("02");
// type2.setName("GIÁM ĐỊNH THƯƠNG MẠI");
// type2.setNote("Kiểm tra chất lượng");

// InspectionType type3 = new InspectionType();
// type3.setInspectionTypeId("03");
// type3.setName("GIÁM ĐỊNH MÔI TRƯỜNG");
// type3.setNote("Kiểm tra môi trường");

// inspectionTypeRepository.saveAll(List.of(type1, type2, type3));
// System.out.println("✅ Seeded default InspectionType data");
// }
// }
// }
