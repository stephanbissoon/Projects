const waitPort = require('wait-port');
const fs = require('fs');
const mysql = require('mysql2');

const {
    MYSQL_HOST: HOST,
    MYSQL_HOST_FILE: HOST_FILE,
    MYSQL_USER: USER,
    MYSQL_USER_FILE: USER_FILE,
    MYSQL_PASSWORD: PASSWORD,
    MYSQL_PASSWORD_FILE: PASSWORD_FILE,
    MYSQL_DB: DB,
    MYSQL_DB_FILE: DB_FILE,
} = process.env;

let pool;

async function init() {
    const host = HOST_FILE ? fs.readFileSync(HOST_FILE) : HOST;
    const user = USER_FILE ? fs.readFileSync(USER_FILE) : USER;
    const password = PASSWORD_FILE ? fs.readFileSync(PASSWORD_FILE) : PASSWORD;
    const database = DB_FILE ? fs.readFileSync(DB_FILE) : DB;

    await waitPort({ 
        host, 
        port: 3306,
        timeout: 10000,
        waitForDns: true,
    });

    pool = mysql.createPool({
        connectionLimit: 5,
        host,
        user,
        password,
        database,
        charset: 'utf8mb4',
    });

    return new Promise((acc, rej) => {
        pool.query(
            'CREATE TABLE IF NOT EXISTS todo_items (id varchar(36), name varchar(255), priority varchar(10), due_date date, '
            + 'category varchar(20), completed boolean) DEFAULT CHARSET utf8mb4',
            err => {
                if (err) return rej(err);

                console.log(`Connected to mysql db at host ${HOST}`);
                acc();
            },
        );
    });
}

async function teardown() {
    return new Promise((acc, rej) => {
        pool.end(err => {
            if (err) rej(err);
            else acc();
        });
    });
}

async function getItems() {
    return new Promise((acc, rej) => {
        pool.query('SELECT * FROM todo_items', (err, rows) => {
            if (err) return rej(err);
            acc(
                rows.map(item =>
                    Object.assign({}, item, {
                        completed: item.completed === 1,
                    }),
                ),
            );
        });
    });
}

async function getItem(id) {
    return new Promise((acc, rej) => {
        pool.query('SELECT * FROM todo_items WHERE id=?', [id], (err, rows) => {
            if (err) return rej(err);
            acc(
                rows.map(item =>
                    Object.assign({}, item, {
                        completed: item.completed === 1,
                    }),
                )[0],
            );
        });
    });
}

async function storeItem(item) {
    return new Promise((acc, rej) => {
        pool.query(
            'INSERT INTO todo_items (id, name, completed) VALUES (?, ?, ?)',
            [item.id, item.name, item.completed ? 1 : 0],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function updateItemCompleted(id, item) {
    return new Promise((acc, rej) => {
        pool.query(
            'UPDATE todo_items SET completed=? WHERE id=?',
            [item.completed ? 1 : 0, id],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function updateItemDueDate(id, item) {
    return new Promise((acc, rej) => {
        pool.query(
            'UPDATE todo_items SET due_date=? WHERE id=?',
            [item.dueDate, id],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function updateItemPriority(id, item) {
    return new Promise((acc, rej) => {
        pool.query(
            'UPDATE todo_items SET priority=? WHERE id=?',
            [item.priority, id],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function updateItemCategory(id, item) {
    return new Promise((acc, rej) => {
        pool.query(
            'UPDATE todo_items SET category=? WHERE id=?',
            [item.category, id],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function updateItemCompleted(id, item) {
    return new Promise((acc, rej) => {
        pool.query(
            'UPDATE todo_items SET completed=? WHERE id=?',
            [item.completed ? 1 : 0, id],
            err => {
                if (err) return rej(err);
                acc();
            },
        );
    });
}

async function removeItem(id) {
    return new Promise((acc, rej) => {
        pool.query('DELETE FROM todo_items WHERE id = ?', [id], err => {
            if (err) return rej(err);
            acc();
        });
    });
}

module.exports = {
    init,
    teardown,
    getItems,
    getItem,
    storeItem,
    updateItemDueDate,
    updateItemPriority,
    updateItemCategory,
    updateItemCompleted,
    removeItem,
};
