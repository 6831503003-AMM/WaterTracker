# 💧 Smart Water Usage Tracking System

A console-based Java application for tracking daily household water consumption, with warnings when usage exceeds a configurable daily limit.

---

## 📁 Project Structure

```
WaterTracker/
├── README.md
├── compile.sh          ← compile all sources
├── run.sh              ← compile + run
└── WT_OODP/
    ├── Main.java                          ← entry point
    │
    ├── model/
    │   └── WaterUsage.java                ← data object (date, amount, category)
    │
    ├── core/
    │   └── WaterTracker.java              ← application controller / menu actions
    │
    ├── exception/
    │   ├── InvalidAmountException.java    ← custom checked exception
    │   ├── InvalidCategoryException.java  ← custom checked exception
    │   └── DataFileException.java         ← custom runtime exception
    │
    ├── repository/
    │   ├── Repository.java                ← generic interface  Repository<T>
    │   └── WaterUsageRepository.java      ← concrete implementation
    │
    ├── report/
    │   ├── Report.java                    ← report interface
    │   ├── DailyReport.java               ← today's summary report
    │   └── FullReport.java                ← full historical report
    │
    ├── io/
    │   └── CsvFileManager.java            ← all file read / write logic
    │
    └── util/
        └── ConsoleHelper.java             ← shared display helpers (bars, menus)
```

---

## ✅ Criteria Coverage

| # | Criterion | Implementation |
|---|-----------|----------------|
| 2.1 | **Read / Write from file** | `io/CsvFileManager` reads and writes `water_data.csv` and `water_config.txt` using `BufferedReader` / `PrintWriter` |
| 2.2 | **Inheritance / Interface** | `report/Report` interface with two concrete implementations: `DailyReport` and `FullReport`; used polymorphically in `WaterTracker` |
| 2.3 | **Exception / Custom Exception** | Three custom exceptions in `exception/`: `InvalidAmountException`, `InvalidCategoryException` (checked), `DataFileException` (runtime); all caught and handled gracefully |
| 2.4 | **Input & Output from keyboard** | `Scanner` in `Main` and `WaterTracker.core` for all menu navigation and data entry |
| 2.5 | **Collection with Generics** | `List<WaterUsage>`, `Map<LocalDate, List<WaterUsage>>` in `WaterUsageRepository`; returned as typed collections throughout |
| 2.6 | **Parametric Polymorphism** | `repository/Repository<T>` — a fully generic interface; `WaterUsageRepository implements Repository<WaterUsage>` |

---

## 🚀 How to Compile & Run

### Option A — shell scripts (Linux / macOS)
```bash
chmod +x compile.sh run.sh

./compile.sh   # compile only
./run.sh       # compile + run
```

### Option B — manual
```bash
# from the WaterTracker/ directory
mkdir -p out
find WT_OODP -name "*.java" | xargs javac -d out
java -cp out WT_OODP.Main
```

### Option C — IDE (IntelliJ / Eclipse / VS Code)
1. Open the `WaterTracker/` folder as the project root.
2. Mark `WaterTracker/` as the sources root (so `WT_OODP` is a top-level package).
3. Run `WT_OODP.Main`.

---

## 💾 Data Files

Both files are created automatically in the working directory on first save.

| File | Contents |
|------|----------|
| `water_data.csv` | One record per line: `YYYY-MM-DD,amount,category` |
| `water_config.txt` | `dailyLimit=<value>` |

---

## 📦 Categories

`shower` · `kitchen` · `laundry` · `garden` · `other`

---

## 🔧 Future Improvements

The package structure is designed so each concern can evolve independently:

- **New export format** (PDF, JSON) → add a class in `io/`
- **New report type** (weekly trend, category pie) → add a class in `report/`
- **Persistent database** → swap `WaterUsageRepository` with a DB-backed implementation of `Repository<T>`
- **GUI / web front-end** → `core/WaterTracker` has no UI dependencies; wire it to any front-end
- **Multiple users** → introduce a `User` model and a `UserRepository<User>`
