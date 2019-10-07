import {
  createStyles,
  Theme,
  Paper,
  Container,
  IconButton,
  Box,
} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import InputBase from "@material-ui/core/InputBase";
import TextField from "@material-ui/core/TextField";
import DeleteIcon from "@material-ui/icons/Delete";
import makeStyles from "@material-ui/styles/makeStyles";
import React, { ChangeEvent } from "react";
import SearchIcon from "@material-ui/icons/Search";
import { withStyles } from "@material-ui/styles";

const styles = (theme: Theme) => ({
  searchButton: {},
});

interface ISearchProps {
  classes: any;
}

interface ISearchState {
  searchQuery: string;
}

class Search extends React.Component<ISearchProps, ISearchState> {
  state: ISearchState = {
    searchQuery: "",
  };

  private onChange = (
    event: ChangeEvent<
      HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
    >
  ) => {
    this.setState({ searchQuery: event.target.value });
  };

  private onKeyPress = (event: any) => {//KeyboardEvent<HTMLDivElement>) => {
    if (event.key === "Enter") {
      console.log(this.state.searchQuery);
      event.preventDefault();
    }
  };

  private doSearch = () => {};

  public render() {
    const { classes } = this.props;

    return (
      <>
        <Grid
          container={true}
          direction="row"
          justify="center"
          alignItems="center"
        >
          <Grid item>
            <TextField
              variant="outlined"
              margin="normal"
              fullWidth={false}
              id="searchSVN"
              name="Search SVN"
              placeholder="Search Query ..."
              autoFocus={true}
              value={this.state.searchQuery}
              onChange={this.onChange}
              onKeyPress={this.onKeyPress}
            />
          </Grid>
          <Grid item>
            <Box>
              <IconButton color="primary">
                <SearchIcon fontSize="large" />
              </IconButton>
            </Box>
          </Grid>
        </Grid>
      </>
    );
  }
}

export default withStyles(styles)(Search);
