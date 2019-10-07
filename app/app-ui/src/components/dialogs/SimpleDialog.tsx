import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
  Tooltip,
  MenuItem,
  Menu,
  Dialog,
  DialogTitle,
  List,
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
} from "@material-ui/core";
import { Theme, withStyles } from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import React from "react";
import InvertColorsIcon from "@material-ui/icons/InvertColors";
import { render } from "react-dom";
import { blue } from "@material-ui/core/colors";
import AddIcon from "@material-ui/icons/Add";
import PersonIcon from "@material-ui/icons/Person";
import { WithStyles } from "@material-ui/styles";
import SimpleDialogTitle from "./SimpleDialogTitle";

const styles = (theme: Theme) => ({
  avatar: {
    backgroundColor: blue[100],
    color: blue[600],
  },
});

interface ISimpleDialogProps {
  open: boolean;
  selectedValue: string;
  title: string;
  onClose: (value: string) => void;
  classes: any;
}

class SimpleDialog extends React.Component<ISimpleDialogProps, {}> {

  public handleClose = () => {
    console.log("The dialog is closed!");
  };


public  render() {
    const { classes } = this.props;

    return (
      <Dialog
        onClose={this.handleClose}
        aria-labelledby="simple-dialog-title"
        open={this.props.open}
      >
        {/* <DialogTitle id="simple-dialog-title">{this.props.title}</DialogTitle> */}
        <SimpleDialogTitle id="hallo" onClose={() => {}}>
          Hallo
          </SimpleDialogTitle>

        

        {this.props.children}
      </Dialog>
    );
  }
}

export default withStyles(styles)(SimpleDialog);
