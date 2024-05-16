import styles from "./services.module.css";
import React, { useState, useEffect } from "react";
import {
  TextField,
  Button,
  Grid,
  Paper,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
} from "@mui/material";
import {
  getTextbooksList,
  createTextbook,
  contactTextbookOwner,
  getUserInformation,
} from "../../apiHelpers";

/**
 * Component for displaying and managing a table of textbook exchange entries.
 *
 * @param {Object} props - Component props.
 * @param {Array} props.data - Array of textbook exchange data.
 * @param {string} props.searchValue - Value to filter the textbook exchange data.
 */
function ExchangeTable(props) {
  const data = props.data;
  const [contacts, setContacts] = useState(data);
  const [textbooks, setTextbooks] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [addFormData, setAddFormData] = useState({
    title: "",
    isbn: "",
    displayName: "",
    location: "",
  });

  const searchValue = props.searchValue.toLowerCase();

  useEffect(() => {
    setContacts(data);
    getUserInformation(setCurrentUser);
  }, [data]);

  const handleAddFormChange = (e) => {
    e.preventDefault();
    const fieldName = e.target.name;
    const fieldValue = e.target.value;

    setAddFormData({
      ...addFormData,
      [fieldName]: fieldValue,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const textbookData = {
      name: addFormData.title,
      isbn: addFormData.isbn,
      displayName: addFormData.displayName,
      location: addFormData.location,
      genreId: 4,
    };

    try {
      // Call createTextbook function to create textbook
      await createTextbook(textbookData);
      alert("A textbook entry was created.");
    } catch (error) {
      // Log error message if an exception occurs
      console.error("Error creating textbook:", error);
    }

    // Reset form fields to empty
    setAddFormData({
      title: "",
      isbn: "",
      displayName: "",
      location: "",
    });

    // Fetch updated list of textbooks
    getTextbooksList(setContacts);
  };

  const handleActionClick = async (contact) => {
    console.log(
      `Clicked action button for textbook with ID: ${contact.textbookId}`
    );

    if (contact.displayName == currentUser.displayName) {
      alert("Cannot exchange with yourself!");
      return;
    }

    try {
      let formData = {
        textbookId: contact.textbookId,
        message: "Interested in this textbook",
      };
      let json = JSON.stringify(formData);
      // Call the contactTextbookOwner API function
      await contactTextbookOwner(json);
      alert("An email was sent to the Textbook owner!");
    } catch (error) {
      console.error("Error contacting textbook owner:", error);
    }
  };

  const filteredData = contacts.filter(
    (book) =>
      !searchValue ||
      book.name.toLowerCase().includes(searchValue) ||
      book.isbn.includes(searchValue) ||
      book.displayName.toLowerCase().includes(searchValue) ||
      book.location.toLowerCase().includes(searchValue)
  );

  return (
    <>
      <div className={styles.TxtExchangeForm}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Paper elevation={3} style={{ padding: "20px" }}>
              <form onSubmit={handleSubmit}>
                <Grid container spacing={2} alignItems="center">
                  <Grid item xs={12} sm={5}>
                    <TextField
                      fullWidth
                      id="title"
                      name="title"
                      label="Title"
                      variant="outlined"
                      margin="normal"
                      required
                      value={addFormData.title}
                      onChange={handleAddFormChange}
                    />
                  </Grid>
                  <Grid item xs={12} sm={4}>
                    <TextField
                      fullWidth
                      id="isbn"
                      name="isbn"
                      label="ISBN"
                      variant="outlined"
                      margin="normal"
                      required
                      value={addFormData.isbn}
                      onChange={handleAddFormChange}
                      inputProps={{
                        pattern: "\\d{13}",
                        title: "ISBN must be exactly 13 numbers long",
                      }}
                    />
                  </Grid>
                  <Grid item xs={12} sm={2}>
                    <FormControl variant="outlined" fullWidth>
                      <InputLabel id="location-label">Location</InputLabel>
                      <Select
                        labelId="location-label"
                        id="location"
                        name="location"
                        value={addFormData.location}
                        onChange={handleAddFormChange}
                        fullWidth
                        required
                        margin="normal"
                        style={{ marginTop: "7px" }}
                        variant="outlined"
                      >
                        <MenuItem value="" disabled>
                          <em>Select Location</em>
                        </MenuItem>
                        <MenuItem value="Toronto">Toronto</MenuItem>
                        <MenuItem value="Mississauga">Mississauga</MenuItem>
                        <MenuItem value="Brampton">Brampton</MenuItem>
                        <MenuItem value="Markham">Markham</MenuItem>
                        <MenuItem value="Vaughan">Vaughan</MenuItem>
                        <MenuItem value="Oakville">Oakville</MenuItem>
                        <MenuItem value="Richmond Hill">Richmond Hill</MenuItem>
                        <MenuItem value="Scarborough">Scarborough</MenuItem>
                        <MenuItem value="Etobicoke">Etobicoke</MenuItem>
                        <MenuItem value="North York">North York</MenuItem>
                        <MenuItem value="Ajax">Ajax</MenuItem>
                        <MenuItem value="Pickering">Pickering</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12} sm={1}>
                    <Button
                      type="submit"
                      variant="contained"
                      fullWidth
                      size="large"
                      sx={{
                        backgroundColor: "#003fa7",
                        "&:hover": {
                          backgroundColor: "#003fa7",
                          opacity: 0.9,
                        },
                      }}
                    >
                      Add
                    </Button>
                  </Grid>
                </Grid>
              </form>
            </Paper>
          </Grid>
        </Grid>
      </div>
      <div className={styles.TxtExchangeTable}>
        <table className={styles.responsiveTable}>
          <thead className={styles.tableHeader}>
            <tr className={styles.tableRow}>
              <th className={`${styles.col} ${styles["col-1"]}`}>
                Textbook Exchange
              </th>
              <th className={`${styles.col} ${styles["col-2"]}`}>ISBN</th>
              <th className={`${styles.col} ${styles["col-3"]}`}>User</th>
              <th className={`${styles.col} ${styles["col-4"]}`}>Location</th>
              <th className={`${styles.col} ${styles["col-5"]}`}>Action</th>
            </tr>
          </thead>
          <tbody>
            {filteredData.map((contact) => (
              <tr key={contact.textbookId}>
                <td className={`${styles.col} ${styles["col-1"]}`}>
                  {contact.name}
                </td>
                <td className={`${styles.col} ${styles["col-2"]}`}>
                  {contact.isbn}
                </td>
                <td className={`${styles.col} ${styles["col-3"]}`}>
                  {contact.displayName}
                </td>
                <td className={`${styles.col} ${styles["col-4"]}`}>
                  {contact.location}
                </td>
                <td className={`${styles.col} ${styles["col-5"]}`}>
                  <button
                    className={styles.exchangeBtn}
                    role="button"
                    onClick={() => handleActionClick(contact)}
                  >
                    Exchange
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default ExchangeTable;
