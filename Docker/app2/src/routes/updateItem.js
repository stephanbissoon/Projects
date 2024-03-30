const db = require('../persistence');

module.exports = async (req, res) => {    
    if(req.body.priority !== undefined) {
        
        await db.updateItemPriority(req.params.id, {
            priority: req.body.priority,
        });
    }

    else if(req.body.dueDate !== undefined) {
        await db.updateItemDueDate(req.params.id, {
            dueDate: req.body.dueDate,
        });
    }

    else if(req.body.category !== undefined) {
        await db.updateItemCategory(req.params.id, {
            category: req.body.category,
        });
    }
    
    else{
        await db.updateItemCompleted(req.params.id, {
            completed: req.body.completed,
        });
    }
    
    const item = await db.getItem(req.params.id);
    res.send(item);
};
