import Footer from './components/Footer';
import Home from './pages/Home';
import Introduction from './pages/Introduction';
import Projects from './pages/Projects';
import React from 'react';
import clsx from 'clsx';
import { withStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import HomeIcon from '@material-ui/icons/Home';
import ProjectsIcon from '@material-ui/icons/Folder';
import IntroductionIcon from '@material-ui/icons/Face'
import UserInfoIcon from '@material-ui/icons/AccountCircle';

const drawerWidth = 240;

const styles = (theme) => ({
  root: {
    display: 'flex',
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: 'nowrap',
  },
  drawerOpen: {
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerClose: {
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: theme.spacing(7) + 1,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing(9) + 1,
    },
  },
  toolbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
  },
});

// const[open, setOpen] = React.useState(false);

class App extends React.Component {
  handleDrawerOpen = () => {
    this.setState({
      open: true
    })
  }

  handleDrawerClose = () => {
    this.setState({
      open: false
    })
  }

  handleProjectsButton = () => {
    this.setState({
      page: 'Projects'
    })
  }    

  handleIntroductionButton = () => {
    this.setState({
      page: 'Introduction'
    })                         
  }

  handleHomeButton = () => {
    this.setState({
      page: 'Home'
    })
  }

  state = {
    open: false,
    setOpen: false,
    page: 'Home'
  }

  render() {
    const { classes } = this.props;
    // const {theme} = useTheme();
    return (
      <div className={classes.root}>
        <CssBaseline />
        <AppBar
          position="fixed"
          className={clsx(classes.appBar, {
            [classes.appBarShift]: this.state.open,
          })}
        >
          <Toolbar>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              onClick={this.handleDrawerOpen}
              edge="start"
              className={clsx(classes.menuButton, {
                [classes.hide]: this.state.open,
              })}
            >
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" noWrap>
              Portfolio
          </Typography>
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          className={clsx(classes.drawer, {
            [classes.drawerOpen]: this.state.open,
            [classes.drawerClose]: !this.state.open,
          })}
          classes={{
            paper: clsx({
              [classes.drawerOpen]: this.state.open,
              [classes.drawerClose]: !this.state.open,
            }),
          }}
          open={this.state.open}
        >
          <div className={classes.toolbar}>
            <IconButton onClick={this.handleDrawerClose}>
              {
                (() => {
                  if (this.state.open === true) return <ChevronLeftIcon />
                  return <ChevronRightIcon />
                })()
              }
            </IconButton>
          </div>
          <Divider />
          <List>
            <ListItem button key='Home' onClick={this.handleHomeButton}>
              <ListItemIcon><HomeIcon  /></ListItemIcon>
              <ListItemText primary='Home' />
            </ListItem>
            <ListItem button key='Introduction' onClick={this.handleIntroductionButton}>
              <ListItemIcon><IntroductionIcon /></ListItemIcon>
              <ListItemText primary='Introduction' />
            </ListItem>
            <ListItem button key='Projects' onClick={this.handleProjectsButton}>
              <ListItemIcon><ProjectsIcon /></ListItemIcon>
              <ListItemText primary='Projects' />
            </ListItem>
            <ListItem button key='HomUser Infoe'>
              <ListItemIcon><UserInfoIcon /></ListItemIcon>
              <ListItemText primary='User Info' />
            </ListItem>
          </List>
          <Divider />
        </Drawer>
        <main className={classes.content}>
          <div className={classes.toolbar} />
          {
            (() => {
              if (this.state.page === 'Home') return <Home />
              if (this.state.page === 'Projects') return <Projects />
              if (this.state.page === 'Introduction') return <Introduction />
            })()
          }
        </main>
        <Footer/>
      </div>
    );
  }
}

export default withStyles(styles)(App);