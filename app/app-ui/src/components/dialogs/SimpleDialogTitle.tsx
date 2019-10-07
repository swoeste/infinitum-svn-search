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
import MuiDialogTitle from "@material-ui/core/DialogTitle";
import CloseIcon from "@material-ui/icons/Close";

const styles = (theme: Theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    // position: "absolute",
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[700]
  },
});

interface IDialogTitleProps {
  id: string;
  children: React.ReactNode;
  onClose: () => void;
  classes: any;
}

class DialogTitle extends React.Component<IDialogTitleProps, {}> {
  public render() {
    const { children, classes, onClose } = this.props;

    return (
      <MuiDialogTitle disableTypography={true} className={this.props.classes.root}>
        <Typography variant="h6">{this.props.children}</Typography>
          <IconButton aria-label="close" className={this.props.classes.closeButton}onClick={onClose}>
            <CloseIcon />
          </IconButton>
      </MuiDialogTitle>
    );
  }
}

export default withStyles(styles)(DialogTitle);
