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

const styles = (theme: Theme) => ({});

interface IFooterProps extends WithStyles<typeof styles> {
}

class Footer extends React.Component<IFooterProps, {}> {
  public render() {
    const { classes } = this.props;

    return <footer><Paper>{this.props.children}</Paper></footer>;
  }
}

export default withStyles(styles)(Footer);
