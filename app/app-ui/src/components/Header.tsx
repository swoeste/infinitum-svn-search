import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
  Tooltip,
  MenuItem,
  Menu,
} from "@material-ui/core";
import { Theme, withStyles } from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import React from "react";
import InvertColorsIcon from "@material-ui/icons/InvertColors";
import SimpleDialog from "./dialogs/SimpleDialog";
import { WithStyles } from "@material-ui/styles";

const styles = (theme: Theme) => ({
  menuButton: {
    marginRight: theme.spacing(2),
  },
  root: {
    flexGrow: 1,
  },
  title: {
    flexGrow: 1,
  },
});

interface IHeaderProps extends WithStyles<typeof styles> {
  onToogleTheme: any;
  classes: any;
}

interface IHeaderState {
  anchorElement: any;
  openDialog1: boolean;
}

class Header extends React.Component<IHeaderProps, IHeaderState> {
  public state = {
    anchorElement: null,
    openDialog1: false,
  };

  public render() {
    const { classes } = this.props;

    return (
      <header>
        <div className={classes.root}>
          <AppBar position="static">
            <Toolbar>
              <IconButton
                id="menuButton"
                edge="start"
                className={classes.menuButton}
                color="inherit"
                aria-label="menu"
                onClick={this.handleClick}
              >
                <MenuIcon />
              </IconButton>

              <Menu
                id="simple-menu"
                anchorEl={this.state.anchorElement}
                keepMounted={true}
                open={Boolean(this.state.anchorElement)}
                onClose={this.handleClose}
              >
                <MenuItem onClick={this.handleClose}>Status</MenuItem>
                <MenuItem onClick={this.handleClose}>Help</MenuItem>
                <MenuItem onClick={this.handleOpenDialog}>
                  Report a problem
                </MenuItem>
              </Menu>

              <SimpleDialog
                selectedValue={"hallo"}
                title={"test"}
                open={this.state.openDialog1}
                onClose={this.handleCloseDialog}
              >
                Hallo Hallo
              </SimpleDialog>

              <Typography variant="h6" className={classes.title}>
                SVN Search
              </Typography>

              <Tooltip title="Switch Light- and Dark-Theme">
                <IconButton onClick={this.props.onToogleTheme}>
                  <InvertColorsIcon />
                </IconButton>
              </Tooltip>
            </Toolbar>
          </AppBar>
        </div>
      </header>
    );
  }

  private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    this.setState({ anchorElement: event.currentTarget });
  };

  private handleClose = () => {
    this.setState({ anchorElement: null });
  };

  private handleCloseDialog = () => {
    this.setState({ openDialog1: false });
  };

  private handleOpenDialog = () => {
    this.setState({ openDialog1: true });
  };
}

export default withStyles(styles)(Header);
