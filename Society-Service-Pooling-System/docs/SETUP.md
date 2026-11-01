# SSPS Setup and Troubleshooting

1. Extract the entire archive.
2. Open VS Code > File > Open Folder.
3. Select the Society-Service-Pooling-System folder containing `src`, `sources.txt`, `run.bat` and `README.md`.
4. Install Extension Pack for Java if needed.
5. Check the JDK in a new terminal:

```powershell
java -version
javac -version
```

If `javac` is not recognised, install a JDK rather than only a JRE. Locate the JDK `bin` folder containing `javac.exe`, add it as a new Windows user PATH entry, and restart all VS Code windows. Do not delete existing PATH entries.

## Windows

```powershell
.\run.bat
```

Manual commands:

```powershell
New-Item -ItemType Directory -Force bin
javac -encoding UTF-8 -d bin '@sources.txt'
java -cp bin ssps.Main
```

Quote `@sources.txt` in PowerShell to avoid special interpretation of the leading `@`.

You may also open `src/ssps/Main.java` and select Run above the main method with the Java extension installed.

## macOS and Linux

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin @sources.txt
java -cp bin ssps.Main
```

## Troubleshooting

- `sources.txt` not found: open the terminal in the project root.
- Main class not found: compile successfully, then use `java -cp bin ssps.Main`.
- New source files not compiled: list them in `sources.txt`.
- Joining blocked: select an OPEN visit in your zone with available capacity and a current/future date.
- Date parsing error: use `YYYY-MM-DD`.
- Selection required: select the relevant table row before clicking an action.
- Saved data cannot load: back it up first; the app stops instead of silently replacing it.
- Old records are not migrated to this renamed package structure.

Always launch from this project root so the same relative data directory is used. Do not use the old single-file compile command or the previous package entry point.
