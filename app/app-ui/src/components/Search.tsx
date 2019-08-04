import { createStyles, Theme } from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import InputBase from "@material-ui/core/InputBase";
import TextField from "@material-ui/core/TextField";
import DeleteIcon from "@material-ui/icons/Delete";
import makeStyles from "@material-ui/styles/makeStyles";
import React from "react";

class Search extends React.Component {
  public render() {
    return (
      <>
        <Grid
          container={true}
          direction="row"
          justify="center"
          alignItems="center"
        >
          <TextField
            variant="outlined"
            margin="normal"
            fullWidth={true}
            id="searchQuery"
            label="searchQuery"
            name="searchQuery"
            autoComplete="searchQuery"
            autoFocus={true}
          />

          <Button variant="contained" color="primary">
            Search
          </Button>
        </Grid>
      </>
    );
  }
}

export default Search;
