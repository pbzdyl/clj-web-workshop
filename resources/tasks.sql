-- name: all-tasks
-- Returns all tasks
SELECT *
FROM "TASKS"

-- name: task-by-id
-- Returns task by ID
SELECT *
FROM "TASKS"
WHERE id = :id

-- name: create-task!
INSERT INTO tasks (id, content, done)
VALUES (:id, :content, :done)

-- name: update-task-by-id!
-- Updates task by ID
UPDATE tasks
SET content = :content,
    done = :done
WHERE id = :id

-- name: delete-task-by-id!
-- Deletes task by ID
DELETE tasks
WHERE id = :id

-- name: update-task-completion-by-id!
-- Updates task completion by ID
UPDATE tasks
SET done = :done
WHERE id = :id
