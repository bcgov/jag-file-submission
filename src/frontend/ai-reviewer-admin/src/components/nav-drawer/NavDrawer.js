import React, { useState } from "react";
import {Link} from "react-router-dom";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Toolbar from "@material-ui/core/Toolbar";
import Drawer from "@material-ui/core/Drawer";
import Hidden from "@material-ui/core/Hidden";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import "components/nav-drawer/NavDrawer.scss";

export default function NavDrawer(props) {
  const {variant} = props;
  const [mobileOpen, setMobileOpen] = useState(false);
  const BaseLink = (props) => <Link to="/" {...props} />;

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const drawer = (
    <>
      <Toolbar />
      <div className="drawer-container">
        <List>
            <ListItem button component={BaseLink} data-testid="nav-item">
              <ListItemText primary="Document Type Configuration" />
            </ListItem>

            <ListItem button >
              <ListItemText primary="Logs" />
            </ListItem>
        </List>
      </div>
    </>
  );

  if (variant === "permanent") {
    return (
      <Hidden smDown>
        <nav className="drawer">
          <Drawer open variant={variant} classes={{ paper: "drawer-paper" }}>
            {drawer}
          </Drawer>
        </nav>
      </Hidden>
    );
  }

  if (variant === "temporary") {
    return (
      <>
        <Hidden mdUp>
          <IconButton
            data-testid="menu-btn"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            className="icon-btn"
          >
            <MenuIcon />
          </IconButton>

          <nav className="drawer">
            <Drawer
              data-testid="modal-drawer"
              variant={variant}
              anchor="left"
              classes={{ paper: "drawer-paper" }}
              open={mobileOpen}
              onClose={handleDrawerToggle}
              ModalProps={{
                keepMounted: true,
              }}
            >
              {drawer}
            </Drawer>
          </nav>
        </Hidden>
      </>
    );
  }
}
