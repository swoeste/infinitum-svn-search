import { red } from "@material-ui/core/colors";
import { createMuiTheme } from "@material-ui/core/styles";

export const lightTheme = createMuiTheme({
  palette: {
    primary: red,
    type: "light",
  },
});

export const darkTheme = createMuiTheme({
  palette: {
    primary: red,
    type: "dark",
  },
});
