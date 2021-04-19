import React, {useState} from "react";
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Toolbar from '@material-ui/core/Toolbar';
import Drawer from '@material-ui/core/Drawer';
import Hidden from '@material-ui/core/Hidden';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import "components/nav-drawer/NavDrawer.scss"

export default function NavDrawer({variant}) {
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const container = window !== undefined ? () => window().document.body : undefined;

  const drawer = (
    <>
      <Toolbar />
      <div className="drawer-container">
        <List>
          {["Nav1", "Nav2", "Nav3", "Nav4", "Nav"].map((text) => (
            <ListItem button key={text}>
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>
      </div>
    </>
  )

  if (variant === "permanent") {
    return (
      <Hidden smDown >
      <nav className="drawer">
        <Drawer open variant={variant} classes={{paper: "drawer-paper"}} >
          {drawer}
        </Drawer>
      </nav>
      </Hidden>
    )
  }

  if (variant === "temporary") {
    return (
      <>
        <Hidden mdUp>
          <IconButton
                aria-label="open drawer"
                edge="start"
                onClick={handleDrawerToggle}
                className="icon-btn"
              >
                <MenuIcon />
          </IconButton>
        </Hidden>

        <Hidden mdUp >
        <nav className="drawer">
          <Drawer
            variant={variant} 
            anchor="left"
            classes={{paper: "drawer-paper"}} 
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
    )
  }
}