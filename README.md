# Task Management API

## Overview
Complete REST API for task management with CRUD operations, subtasks, and linked work items.

## Features
- ✅ Create, read, update, delete tasks
- ✅ Assign tasks to users
- ✅ Update task status and assignee independently
- ✅ Add subtasks to main tasks
- ✅ Link related work items
- ✅ Proper error handling and validation

## API Endpoints

### Tasks
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/tasks` | Create a new task |
| `GET` | `/tasks/{id}` | Get task by ID |
| `GET` | `/tasks/project/{projectId}` | Get all tasks for a project |
| `GET` | `/tasks/assignee/{assigneeId}` | Get tasks by assignee |
| `PUT` | `/tasks/{id}` | Update a task |
| `PATCH` | `/tasks/{id}/status` | Update task status |
| `PATCH` | `/tasks/{id}/assignee` | Update task assignee |
| `DELETE` | `/tasks/{id}` | Delete a task |

### Subtasks
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/tasks/{taskId}/subtasks` | Add subtask to a task |
| `GET` | `/tasks/{taskId}/subtasks` | Get all subtasks for a task |

### Linked Work Items
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/tasks/{taskId}/linked-work-items/{linkedTaskId}` | Link tasks together |

## Task Status Values
- `TO_DO` - Task is pending
- `IN_PROGRESS` - Task is being worked on
- `DONE` - Task is completed
- `CANCELLED` - Task is cancelled

## Priority Levels
- `LOW` - Low priority
- `MEDIUM` - Medium priority (default)
- `HIGH` - High priority

## Request Headers
- `Content-Type: application/json`
- `X-User-Id: {user-uuid}` (for user context)

## Example Usage

### Create a Task
```http
POST /tasks
Content-Type: application/json
X-User-Id: 11111111-1111-1111-1111-111111111111

{
  "title": "Implement User Authentication",
  "description": "Create login system",
  "projectId": "project-uuid-here",
  "assigneeId": "user-uuid-here",
  "dueDate": "2024-12-31T23:59:59.000Z",
  "priority": "HIGH"
}