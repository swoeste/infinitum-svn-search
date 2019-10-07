import Button from "@material-ui/core/Button";
import Container from "@material-ui/core/Container";
import CssBaseline from "@material-ui/core/CssBaseline";
import Grid from "@material-ui/core/Grid";
import DeleteIcon from "@material-ui/icons/Delete";
import { ThemeProvider } from "@material-ui/styles";
import React from "react";
import { BrowserRouter as Router, Link, Route } from "react-router-dom";
import "./App.css";
import Header from "./components/Header";
import Result from "./components/Result";
import Search from "./components/Search";
import { darkTheme, lightTheme } from "./components/Theme";
import Cookies from "universal-cookie";
import Content from "./components/Content";

interface IAppProps {
  searchQuery: string;
}

interface IAppState {
  searchQuery: string;
  useDarkTheme: boolean;
}

class App extends React.Component<{}, IAppState> {
  private cookies = new Cookies();

  public constructor(props: {}) {
    super(props);
    this.state = {
      searchQuery: "",
      useDarkTheme: this.isUseDarkTheme(),
    };
  }

  private isUseDarkTheme = (): boolean => {
    return Boolean(this.cookies.get("use.dark.theme"));
  };

  public onToogleTheme = () => {
    let useDarkTheme: boolean = !this.state.useDarkTheme;
    this.setState({ useDarkTheme: useDarkTheme });
    this.cookies.set("use.dark.theme", useDarkTheme);
  };

  public getActiveTheme = () => {
    return this.state.useDarkTheme ? darkTheme : lightTheme;
  };

  public render() {
    return (
      <ThemeProvider theme={this.getActiveTheme()}>
        <CssBaseline />
        <Container>
          <Header onToogleTheme={this.onToogleTheme} />
          <Content>
            <Router>
              <div>
                <Route path="/search" component={Search} />
                <Route path="/result" component={Result} />
              </div>
            </Router>
          </Content>
        </Container>
      </ThemeProvider>
    );
  }
}

export default App;
