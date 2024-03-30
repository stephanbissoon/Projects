function App() {
    const { Container, Row, Col } = ReactBootstrap;
    
    console.log("App accessed on port 3001");
    
    return (
        <Container>
            <Row>
                <Col md={{ offset: 3, span: 6 }}>
                    <TodoListCard />
                </Col>
            </Row>
        </Container>
    );
}

function TodoListCard() {
    const [items, setItems] = React.useState(null);
    const [searchTerm, setSearchTerm] = React.useState(''); // need for search and filter

    //simple function that maps the user inputs to filter the items array 
    const filteredItems = items && items.filter(item => {
        return item.name.toLowerCase().includes(searchTerm.toLowerCase());
    });

    React.useEffect(() => {
        fetch('/items')
            .then(r => r.json())
            .then(setItems);
    }, []);

    const onNewItem = React.useCallback(
        newItem => {
            setItems([...items, newItem]);
        },
        [items],
    );

    const onItemUpdate = React.useCallback(
        item => {
            const index = items.findIndex(i => i.id === item.id);
            setItems([
                ...items.slice(0, index),
                item,
                ...items.slice(index + 1),
            ]);
        },
        [items],
    );

    const onItemRemoval = React.useCallback(
        item => {
            const index = items.findIndex(i => i.id === item.id);
            setItems([...items.slice(0, index), ...items.slice(index + 1)]);
        },
        [items],
    );

    if (items === null) return 'Loading...';

    return (
        <React.Fragment>
            <AddItemForm onNewItem={onNewItem} />
            {items.length === 0 && (
                <p className="text-center">You have no todo items yet! Add one above!</p>
            )}

            <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            
            {/* Use filteredItems.map instead of items.map to render ItemDisplay components */}
            {filteredItems && filteredItems.length > 0 ? (
                filteredItems.map(item => (
                    <ItemDisplay
                        item={item}
                        // ... other props
                        key={item.id}
                        onItemUpdate={onItemUpdate}
                        onItemRemoval={onItemRemoval}
                    />
                ))
            ) : (
                <p className="text-center">No items found.</p>
            )}
        </React.Fragment>
    );
}

function AddItemForm({ onNewItem }) {
    const { Form, InputGroup, Button } = ReactBootstrap;

    const [newItem, setNewItem] = React.useState('');
    const [submitting, setSubmitting] = React.useState(false);

    const submitNewItem = e => {
        e.preventDefault();
        setSubmitting(true);
        fetch('/items', {
            method: 'POST',
            body: JSON.stringify({ name: newItem }),
            headers: { 'Content-Type': 'application/json' },
        })
            .then(r => r.json())
            .then(item => {
                onNewItem(item);
                setSubmitting(false);
                setNewItem('');
            });
    };

    return (
        <Form onSubmit={submitNewItem}>
            <InputGroup className="mb-3">
                <Form.Control
                    value={newItem}
                    onChange={e => setNewItem(e.target.value)}
                    type="text"
                    placeholder="New Item"
                    aria-describedby="basic-addon1"
                />
                <InputGroup.Append>
                    <Button
                        type="submit"
                        variant="success"
                        disabled={!newItem.length}
                        className={submitting ? 'disabled' : ''}
                    >
                        {submitting ? 'Adding...' : 'Add Item'}
                    </Button>
                </InputGroup.Append>
            </InputGroup>
        </Form>
    );
}

function ItemDisplay({ item, onItemUpdate, onItemRemoval }) {
    const { Container, Row, Col, Button } = ReactBootstrap;

    const toggleCompletion = () => {
        fetch(`/items/${item.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                completed: !item.completed,
            }),
            headers: { 'Content-Type': 'application/json' },
        })
            .then(r => r.json())
            .then(onItemUpdate);
    };

    const removeItem = () => {
        fetch(`/items/${item.id}`, { method: 'DELETE' }).then(() =>
            onItemRemoval(item),
        );
    };

    const updatePriority = (event) => {
        let value = event.target.value;

        fetch(`/items/${item.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                priority: value,
            }),
            headers: { 'Content-Type': 'application/json' },
        })
        .then(r => r.json())
        .then(onItemUpdate);
    };

    const updateDueDate = (event) => {
        let value = event.target.value;
    
        fetch(`/items/${item.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                dueDate: value,
            }),
            headers: { 'Content-Type': 'application/json' },
        })
        .then(r => r.json())
        .then(onItemUpdate);
    };

    const updateCategory = (event) => {
        let value = event.target.value;

        fetch(`/items/${item.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                category: value,
            }),
            headers: { 'Content-Type': 'application/json' },
        })
        .then(r => r.json())
        .then(onItemUpdate);
    };
    

    const Priority = (valObj) => {
        const { Form } = ReactBootstrap;
        
        return (    
            <Form.Control as="select" onChange={updatePriority} value={valObj.val}>
                <option value="High">High</option>
                <option value="Medium">Medium</option>
                <option value="Low">Low</option>
            </Form.Control>
        )
    };

    const DueDate = (valObj) => {
        const { Form } = ReactBootstrap;

        let parsedDate = null;

        if(valObj.val !== undefined && valObj.val !== null) {
            parsedDate = new Date(valObj.val).toISOString().split("T")[0];
        }

        return (
            <Form.Control
                type="date"
                onChange={updateDueDate}
                value={parsedDate}
            />
        )
    }

    const Category = (valObj) => {
        const { Form } = ReactBootstrap;
        return (    
            <Form.Control as="select" onChange={updateCategory} value={valObj.val}>
                <option value="Personal">Personal</option>
                <option value="Work">Work</option>
                <option value="Travel">Travel</option>
                <option value="Social">Social</option>
                <option value="Meeting">Meeting</option>
            </Form.Control>
        )

    }

    return (
        <Container fluid className={`item ${item.completed && 'completed'}`}>
            <Row>
                <Col xs={1} className="text-center">
                    <Button
                        className="toggles"
                        size="sm"
                        variant="link"
                        onClick={toggleCompletion}
                        aria-label={
                            item.completed
                                ? 'Mark item as incomplete'
                                : 'Mark item as complete'
                        }
                    >
                        <i
                            className={`far ${
                                item.completed ? 'fa-check-square' : 'fa-square'
                            }`}
                        />
                    </Button>
                </Col>
                <Col xs={2} className="name">
                    {item.name}
                </Col>
                <Col xs={2}>
                    <Priority
                        val={item.priority}
                    />
                </Col>
                <Col xs={3}>
                    <DueDate
                        val={item.due_date}
                    />
                </Col>
                <Col xs={3}>
                    <Category
                        val={item.category}
                    />
                </Col>
                <Col xs={1} className="text-center remove">
                    <Button
                        size="sm"
                        variant="link"
                        onClick={removeItem}
                        aria-label="Remove Item"
                    >
                        <i className="fa fa-trash text-danger" />
                    </Button>
                </Col>
            </Row>
        </Container>
    );
}

ReactDOM.render(<App />, document.getElementById('root'));
