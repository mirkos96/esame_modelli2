/*
Run this script to create indexes for all entities
*/

db.bookmarks.ensureIndex({ user_id : 1})
db.bookmarks.ensureIndex({ created_on : -1})
db.bookmarks.ensureIndex({ modified_at : -1})
db.bookmarks.ensureIndex({ is_deleted : 1 })

db.notes.ensureIndex({ user_id : 1 })
db.notes.ensureIndex({ created_on : -1})
db.notes.ensureIndex({ modified_at : -1})
db.notes.ensureIndex({ is_deleted : 1 })

db.pins.ensureIndex({ user_id : 1 })
db.pins.ensureIndex({ created_on : -1})
db.pins.ensureIndex({ modified_at : -1})
db.pins.ensureIndex({ is_deleted : 1 })

db.activities.ensureIndex({ user_id : 1 })
db.activities.ensureIndex({ created_on : -1})

db.todos.ensureIndex({ user_id : 1})
db.todos.ensureIndex({ created_on : -1})
db.todos.ensureIndex({ modified_at : -1})
db.todos.ensureIndex({ is_deleted : 1})

db.expenses.ensureIndex({ user_id : 1 })
db.expenses.ensureIndex({ created_on : -1 })
db.expenses.ensureIndex({ modified_at : -1 })
db.expenses.ensureIndex({ is_deleted : 1})
db.expenses.ensureIndex({ date : 1 })
db.expenses.ensureIndex({ amount : 1 })
db.expenses.ensureIndex({ categories : 1 })

db.diaries.ensureIndex({ user_id : 1 })
db.diaries.ensureIndex({ created_on : -1 })
db.diaries.ensureIndex({ modified_at : -1 })
db.diaries.ensureIndex({ is_deleted : 1 })
db.diaries.ensureIndex({ year : 1 })
db.diaries.ensureIndex({ month : 1 })
db.diaries.ensureIndex({ date : 1 })

db.users.ensureIndex({ email : 1}, { unique : true})