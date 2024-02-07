# SMART LICENSE System

## Contributors
- [Muhammad Fuzail Raza](https://github.com/Fuzail-Raza)
- [Waqas Zafar](https://github.com/WaqasZafar9)

## Overview
Smart License System is a groundbreaking project aimed at revolutionizing the traditional driving license process. By seamlessly integrating modern technologies, the system enhances efficiency and user-friendliness, providing a streamlined experience for our local community.

## Key Features

### User Registration
- Users can register and obtain a unique learner ID for the license process.

### Testing Module
- Comprehensive computer-based and practical driving tests for evaluation.

### License Issuance
- Issuance of learner's and permanent licenses with digital notifications and optional home delivery.

### Administrative Interface
- Dual Dashboards: Admin login for system operators to manage users, questions, and license approvals.

## Project Workflow
1. **User Registration:** Users can register and receive a unique ticket for a streamlined process.
2. **Issue Learner to Driver:** Learner's license issued with a validity of 6 months after fulfilling all requirements.
3. **Practice Driving:** Drivers are eligible to take the Symbol Test after 41 days.
4. **Testing Phases:** Comprehensive computer-based and practical driving tests for thorough evaluation.
5. **Driving Test:** If both tests pass, the permanent license is issued with a validity of 5 years.
6. **License Issuance:** Successful candidates receives a permanent license.


## Digital Notifications
Users are kept informed about their application status and test results through digital notifications.

## System Operator Workflow
1. **Issue Learner to Driver:** Learner's license issued with a validity of 6 months after fulfilling all requirements.
2. **Practice Driving:** Drivers are eligible to take the Symbol Test after 41 days.
3. **Symbol Test:** If passed, drivers proceed to the Driving Test; otherwise, they wait for another 41 days.
4. **Driving Test:** If both tests pass, the permanent license is issued with a validity of 5 years.

## Technologies Used
- **Programming Language:** Java
- **Database:** MongoDB
- **User Interface:** JavaFX
- **SMS API:** [infobip.com](https://infobip.com)
- **Package Manager:** Maven

## Running the Application
1. Add Database URL in `DataBase.java` file.
2. Add SMS API from [infobip.com](https://infobip.com) in `sms.java` file.
3. Run `AddAdmin.java` in `src/main/java/Admin/AddAdmin.java` to add the first admin.
4. After completing the above steps, run `AdminLogin.java` in `src/main/java/Admin/AdminLogin.java` or `src/test/java/LicenseMain`.
5. Add System Operators in the Admin Panel.

## Admin Operations
- Update System Operator Information
- Add System Operator
- Delete System Operator
- Add Symbol for Test
- Delete Symbol for Test
- Update Symbol for Test
- Display System Operator Information
- Display Drivers Information
- Add Another Admin

## System Operator Portal (User)
### Operations
- License Test Form
- Add Driver Info (Issue Learner)
- Symbol Test
- Update License
- Update Driver Information
- logout

### System Operator Workflow
1. `addDriverInfo` -> 2. `symbolTest` -> 3. `licenseTestForm`

## Project Impact
The Smart License System signifies a significant leap in driving license management, seamlessly blending technology and make zero Paper usage.

## Practical Application
The project demonstrates the practical implementation of Java programming skills, setting a benchmark for real-world solutions.

## License
This project is licensed under the **MIT License** - see the LICENSE file for details.

## Contributions
Contributions to this project are welcome. If you find any issues or want to add new features, feel free to open an issue or submit a pull request.  
> ### Contact me via email: [waqaszafar771@gmail.com](mailto:waqaszafar771@gmail.com)  
