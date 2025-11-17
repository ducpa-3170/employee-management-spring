# AOP Logging Implementation Guide

## Tổng quan
Dự án đã được cấu hình AOP (Aspect-Oriented Programming) để tự động ghi logging cho các controller và service methods.

## Cấu trúc

### 1. Dependencies
- `spring-boot-starter-aop`: Được thêm vào `pom.xml`

### 2. Các file đã tạo
- `aspect/Loggable.java`: Custom annotation để đánh dấu method cần logging chi tiết
- `aspect/LoggingAspect.java`: Aspect class xử lý logging

### 3. Cấu hình
- `@EnableAspectJAutoProxy` đã được thêm vào `EmployeeManagementApplication.java`

## Cách hoạt động

### Automatic Logging (Tự động)

#### Controller Methods
Tất cả các method trong package `controllers` sẽ **tự động** được log:
- Log khi nhận request
- Log khi trả về response
- Log execution time
- Log exception nếu có

```java
// Controller method - KHÔNG cần thêm annotation gì
@GetMapping("/employees")
public String listEmployees(Model model) {
    // Code của bạn
}

// Output log:
// >>> Controller - EmployeeController.listEmployees - Request received
// <<< Controller - EmployeeController.listEmployees - Response sent (45 ms)
```

#### Service Methods
Tất cả các method trong package `services` sẽ **tự động** được log:
- Log khi bắt đầu thực thi
- Log khi hoàn thành
- Log execution time
- Log exception nếu có

```java
// Service method - KHÔNG cần thêm annotation gì
public List<Employee> getAllEmployees() {
    // Code của bạn
}

// Output log (DEBUG level):
// >> Service - EmployeeService.getAllEmployees - Started
// << Service - EmployeeService.getAllEmployees - Completed (23 ms)
```

### Custom Logging với @Loggable

Để có logging chi tiết hơn (parameters, result), sử dụng `@Loggable`:

```java
import com.example.employee_management.aspect.Loggable;

@Loggable
public Employee createEmployee(Employee employee) {
    // Code của bạn
    return savedEmployee;
}

// Output log:
// ==> Entering method: EmployeeService.createEmployee
//     Parameters: [Employee(id=null, name=John Doe, ...)]
//     Execution time: 156 ms
//     Result: Employee(id=1, name=John Doe, ...)
// <== Exiting method: EmployeeService.createEmployee
```

### Tùy chỉnh @Loggable

```java
// Chỉ log execution time, không log params và result
@Loggable(logParams = false, logResult = false, logExecutionTime = true)
public void processLargeData(byte[] data) {
    // Code của bạn
}

// Chỉ log params, không log result
@Loggable(logParams = true, logResult = false)
public void updateEmployee(Long id, Employee employee) {
    // Code của bạn
}
```

## Log Levels

### Controller logs: INFO level
```
>>> Controller - EmployeeController.listEmployees - Request received
<<< Controller - EmployeeController.listEmployees - Response sent (45 ms)
```

### Service logs: DEBUG level
```
>> Service - EmployeeService.getAllEmployees - Started
<< Service - EmployeeService.getAllEmployees - Completed (23 ms)
```

### @Loggable logs: INFO level
```
==> Entering method: EmployeeService.createEmployee
    Parameters: [...]
    Execution time: 156 ms
    Result: ...
<== Exiting method: EmployeeService.createEmployee
```

### Exception logs: ERROR level
```
!!! Controller - EmployeeController.createEmployee - Exception occurred after 12 ms: Validation failed
!! Service - EmployeeService.saveEmployee - Exception after 8 ms: Database connection error
```

## Ví dụ sử dụng

### Ví dụ 1: Controller (Automatic)
```java
@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    // Tự động có logging - KHÔNG cần thêm annotation
    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employees/list";
    }
}
```

### Ví dụ 2: Service (Automatic)
```java
@Service
public class EmployeeService {

    // Tự động có logging - KHÔNG cần thêm annotation
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
```

### Ví dụ 3: Service với @Loggable (Chi tiết)
```java
@Service
public class EmployeeService {

    // Logging chi tiết với params và result
    @Loggable
    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        return employeeRepository.save(employee);
    }

    // Chỉ log execution time (cho method xử lý dữ liệu lớn)
    @Loggable(logParams = false, logResult = false)
    public void importEmployeesFromExcel(MultipartFile file) {
        // Import logic
    }
}
```

### Ví dụ 4: Custom method (không phải Controller/Service)
```java
@Component
public class ReportGenerator {

    // Sử dụng @Loggable cho method cần logging
    @Loggable
    public Report generateMonthlyReport(int month, int year) {
        // Report generation logic
        return report;
    }
}
```

## Lợi ích

1. **Tự động logging**: Controller và Service methods tự động có logging
2. **Không xâm nhập code**: Không cần thay đổi business logic
3. **Dễ debug**: Theo dõi được flow của application
4. **Performance monitoring**: Tự động log execution time
5. **Exception tracking**: Tự động log khi có lỗi xảy ra
6. **Linh hoạt**: Có thể tùy chỉnh với @Loggable

## Best Practices

1. **Controller/Service**: Không cần thêm @Loggable, đã tự động có logging
2. **Custom classes**: Sử dụng @Loggable khi cần logging chi tiết
3. **Sensitive data**: Tắt logParams khi method nhận sensitive data (password, credit card, ...)
4. **Large data**: Tắt logParams và logResult cho method xử lý dữ liệu lớn
5. **Production**: Đặt service log level = INFO trong production (hiện tại là DEBUG)

## Cấu hình Log Level

Trong `application.yml`:
```yaml
logging:
  level:
    com.example.employee_management.aspect.LoggingAspect: DEBUG
    # Hoặc INFO cho production
```

## Troubleshooting

Nếu logging không hoạt động:
1. Kiểm tra `@EnableAspectJAutoProxy` trong main class
2. Kiểm tra dependency `spring-boot-starter-aop` trong pom.xml
3. Đảm bảo class có annotation @Component hoặc @Service
4. Kiểm tra log level trong application.yml
