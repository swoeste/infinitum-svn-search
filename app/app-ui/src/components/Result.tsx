import React from "react";
import { withRouter, WithRouterProps, RouteComponentProps } from "react-router";




interface IResultProps extends RouteComponentProps {
  onToogleTheme: any;
  classes: any;
}


class Result extends React.Component<IResultProps, {}> {
  public render() {
    console.log(this.props)
    return <>{this.props.location.search}</>;
  }
}

export default withRouter(Result);
