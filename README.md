# 📋 IPMS - Internship Placement Management System

## SC2002 Object-Oriented Design & Programming Assignment

### Project Overview

The Internship Placement Management System (IPMS) is a comprehensive Java-based CLI application designed to manage internship opportunities, student applications, and placement processes. The system facilitates interactions between three user types: Students, Company Representatives, and Career Center Staff.

---

## 🎯 Features

### For Students:
- View available internship opportunities based on eligibility
- Apply for up to 3 internships simultaneously
- View application status and history
- Accept placement offers
- Request withdrawal from applications

### For Company Representatives:
- Register and await approval
- Create up to 5 internship opportunities
- View and manage applications
- Approve/reject student applications
- Toggle visibility of internship postings

### For Career Center Staff:
- Approve company representative registrations
- Review and approve internship opportunities
- Handle withdrawal requests
- Generate comprehensive reports with filtering options
- Monitor all system activities

---

## 🏗️ System Architecture

### Design Patterns Implemented:
- **Singleton Pattern**: Managers (UserManager, InternshipManager, ApplicationManager, AuthenticationManager)
- **Strategy Pattern**: Filtering system (StatusFilter, LevelFilter, MajorFilter, etc.)
- **Composite Pattern**: Combined filters for complex queries
- **Inheritance & Polymorphism**: User hierarchy (Student, CompanyRepresentative, CareerCenterStaff)

### Package Structure:
```
ipms/
├── boundary/          # User Interface Layer
│   ├── MainUI.java
│   ├── StudentUI.java
│   ├── CompanyRepUI.java
│   ├── StaffUI.java
│   └── InputValidator.java
│
├── control/           # Business Logic Layer
│   ├── AuthenticationManager.java
│   ├── UserManager.java
│   ├── InternshipManager.java
│   ├── ApplicationManager.java
│   ├── ReportGenerator.java
│   └── [Filter Classes]
│
└── entity/            # Data Model Layer
    ├── User.java (abstract)
    ├── Student.java
    ├── CompanyRepresentative.java
    ├── CareerCenterStaff.java
    ├── InternshipOpportunity.java
    ├── Application.java
    ├── WithdrawalRequest.java
    └── [Enum Classes]
```

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command-line terminal or Java IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. **Clone or download the project:**
   ```bash
   git clone https://github.com/darthrevan030/TUI-Internship-Placement-Management-System
   cd sc2002-ipms
   ```

2. **Prepare the data directory:**
   ```bash
   mkdir -p data
   ```

3. **Add CSV files to the data folder:**
   - `sample_student_list.csv`
   - `sample_staff_list.csv`
   - `sample_company_representative_list.csv` (optional)

---

## 🔨 Compilation

### Option 1: Using Command Line

```bash
# Navigate to src directory
cd src

# Compile all Java files
javac ipms/**/*.java -d ../bin

# Verify compilation
ls ../bin/ipms
```

### Option 2: Using IDE

**IntelliJ IDEA:**
1. Open project folder
2. Right-click on `src` → Mark Directory as → Sources Root
3. Build → Build Project (Ctrl+F9)

**Eclipse:**
1. File → Import → Existing Projects into Workspace
2. Select project folder
3. Project → Build Automatically (ensure checked)

**VS Code:**
1. Open project folder
2. Install "Extension Pack for Java"
3. Press F5 to build and run

---

## ▶️ Running the Application

### From Command Line:

```bash
# Navigate to bin directory
cd bin

# Run the application
java ipms.boundary.MainUI
```

### From IDE:
Run the `MainUI.java` file directly (Right-click → Run)

---

## 👥 Default Login Credentials

### Student Accounts:
- **User ID**: Any from `sample_student_list.csv` (e.g., `U1234567A`)
- **Password**: `password`

### Career Center Staff:
- **User ID**: Any from `sample_staff_list.csv` (e.g., `STAFF001`)
- **Password**: `password`

### Company Representatives:
- Register through the main menu (Option 2)
- Wait for approval from Career Center Staff
- Then login with registered email and password

---

## 📊 Sample CSV File Formats

### sample_student_list.csv
```csv
StudentID,Name,Major,Year,Email
U1234567A,Alice Tan,CSC,3,alice.tan@student.ntu.edu.sg
U2345678B,Bob Lee,EEE,2,bob.lee@student.ntu.edu.sg
U3456789C,Charlie Wong,MAE,4,charlie.wong@student.ntu.edu.sg
```

### sample_staff_list.csv
```csv
StaffID,Name,Role,Department,Email
STAFF001,Dr. Sarah Chen,Career Advisor,Career Services,sarah.chen@ntu.edu.sg
STAFF002,Mr. James Lim,Senior Advisor,Career Services,james.lim@ntu.edu.sg
```

---

## 🧪 Testing

### Test Scenarios

**1. Student Application Flow:**
```
1. Login as Student (U1234567A / password)
2. View Internship Opportunities
3. Apply for an internship
4. View My Applications
5. Logout
```

**2. Company Representative Flow:**
```
1. Register as Company Rep
2. Login as Staff (STAFF001 / password)
3. Approve the new Company Rep
4. Logout and login as Company Rep
5. Create Internship Opportunity
6. Logout
```

**3. Staff Approval Flow:**
```
1. Login as Staff (STAFF001 / password)
2. Approve Internship Opportunities
3. Review and approve applications
4. Generate Reports
```

**4. Complete Application Cycle:**
```
1. Company Rep creates internship
2. Staff approves internship
3. Student applies for internship
4. Company Rep approves application
5. Student accepts placement
6. Verify slot count updates
```

---

## 📖 Generating Javadoc

### From Command Line:

```bash
cd src
javadoc -d ../docs -author -private -noqualifier all -version ipms.entity ipms.control ipms.boundary
```

### View Documentation:
Open `docs/index.html` in a web browser

---

## 🗂️ Data Persistence

The system uses Java serialization for data persistence:

- **users.dat**: Stores all user accounts
- **internships.dat**: Stores internship opportunities
- **applications.dat**: Stores applications and withdrawal requests

**Location**: `data/` directory (created automatically on first run)

**⚠️ Note**: Delete `.dat` files to reset the system to initial state (will reload from CSV)

---

## 🔑 Key Business Rules

1. **Students**:
   - Maximum 3 concurrent applications
   - Year 1-2: Can only apply for BASIC level internships
   - Year 3-4: Can apply for any level
   - Can only accept 1 placement (withdraws all other applications)

2. **Company Representatives**:
   - Maximum 5 internship opportunities
   - Must be approved by staff before login
   - Can only manage their own internships

3. **Internship Opportunities**:
   - Maximum 10 slots per internship
   - Visibility can be toggled after approval
   - Status automatically changes to FILLED when all slots are taken

4. **Withdrawals**:
   - Require approval from Career Center Staff
   - Releases slot if withdrawal is from accepted placement

---

## 🛠️ Troubleshooting

### Problem: "package ipms.entity does not exist"
**Solution:**
1. Clean and rebuild your IDE
2. Verify all files are in correct folders
3. Try command-line compilation to verify code

### Problem: "Cannot find CSV files"
**Solution:**
1. Ensure `data/` folder exists in project root
2. Check CSV file names match exactly
3. Run from correct directory (project root or bin)

### Problem: "Login fails with correct credentials"
**Solution:**
1. For Company Reps: Ensure account is approved by staff
2. Check password (default: "password")
3. Delete `data/*.dat` files to reset

### Problem: "No internships visible to student"
**Solution:**
1. Staff must approve internship first
2. Company Rep must set visibility to ON
3. Check student's major matches internship's preferred major
4. Check student's year matches internship level

---

## 📝 Project Structure

```
TUI-Internship-Placement-Management-System/
├── src/
│   └── ipms/
│       ├── boundary/         (5 files)
│       ├── control/          (10 files)
│       └── entity/           (11 files)
├── bin/                      (compiled .class files)
├── data/
│   ├── sample_student_list.csv
│   ├── sample_staff_list.csv
│   ├── users.dat             (generated)
│   ├── internships.dat       (generated)
│   └── applications.dat      (generated)
├── docs/                     (Javadoc output)
└── README.md
```

---

## 👨‍💻 Authors

SC2002 Group 1
- SAMARTH BHATIA
- MADHAV RAGHU ANANTHARAM
- LIM MING WEN
- TAN CHOON YANG
- TOH JUN MENG

---

## 📚 Assignment Requirements

This project fulfills the requirements for:
- **Course**: SC2002 Object-Oriented Design & Programming
- **Semester**: 2025/2026 Semester 1
- **Institution**: Nanyang Technological University (NTU)

---

## 📄 License

This project is for educational purposes as part of the SC2002 course assignment.

---

**Last Updated**: October 2025  
**Version**: 1.0

---

## 🎯 Quick Start Summary

```bash
# 1. Compile
cd src
javac ipms/**/*.java -d ../bin

# 2. Run
cd bin
java ipms.boundary.MainUI

# 3. Login as Student
# User ID: U1234567A
# Password: password

# 4. Explore the system!
```