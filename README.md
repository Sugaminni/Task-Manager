# Java Task Manager

A CLI based task manager built with Java, designed to help users organize, prioritize, and manage their tasks across custom folders. The application supports advanced features like batch operations, undo/redo, folder management, and file import/export.

## Features

- **Task Management**: Add, edit, delete, and view tasks with details like due date, priority, workload, and completion status.
- **Folder System**: Organize tasks by folders using a dynamic structure (`Map<String, List<Task>>`). Supports creating, renaming, deleting, and switching folders.
- **Search and Filter**: Search tasks by title/description or filter by status, due date, priority, and workload.
- **Sorting**: Sort tasks by due date, priority, workload, or completion status using dedicated comparator classes.
- **Batch Operations**: Perform batch deletions or mark multiple tasks as complete using comma-separated input.
- **Undo/Redo**: Reverse or reapply your most recent actions using stack-based state tracking.
- **File Import/Export**:
  - Export tasks to `.csv` format
  - Import from `.csv` and `.md` with validation and parsing
  - Future support for `.json` and `.txt`
- **Modular Design**: Clean separation of concerns using utility and manager classes to enhance maintainability and scalability.


