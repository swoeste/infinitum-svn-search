import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
  Tooltip,
  MenuItem,
  Menu,
  Paper,
} from "@material-ui/core";
import { Theme, withStyles, WithStyles } from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import React from "react";
import InvertColorsIcon from "@material-ui/icons/InvertColors";
import SimpleDialog from "./dialogs/SimpleDialog";

const styles = (theme: Theme) => ({
  content: {
    height: "100%",
    padding: theme.spacing(8, 0, 6),
  },
});

interface IContentProps extends WithStyles<typeof styles> {}

class Content extends React.Component<IContentProps, {}> {
  public render() {
    const { classes } = this.props;
    return (
      <main>
        <Paper className={classes.content} square={true}>
          {this.props.children}
        </Paper>
      </main>
    );
  }
}

export default withStyles(styles)(Content);
