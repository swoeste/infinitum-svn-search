import {
  AppBar,
  Button,
  IconButton,
  Toolbar,
  Typography,
} from "@material-ui/core";
import { Theme, withStyles } from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import React from "react";

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

interface IHeaderProps {
  onToogleTheme: any;
  classes: any;
}

class Header extends React.Component<IHeaderProps, {}> {
  public render() {
    const { classes } = this.props;

    return (
      <div className={classes.root}>
        <AppBar position="static">
          <Toolbar>
            <IconButton
              edge="start"
              className={classes.menuButton}
              color="inherit"
              aria-label="menu"
            >
              <MenuIcon />
            </IconButton>

            <Typography variant="h6" className={classes.title}>
              SVN Search
            </Typography>

            <Button onClick={this.props.onToogleTheme}>SWITCH</Button>
          </Toolbar>
        </AppBar>
      </div>
    );
  }
}

export default withStyles(styles)(Header);
