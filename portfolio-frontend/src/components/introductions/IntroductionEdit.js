import { withStyles } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import IconButton from '@material-ui/core/IconButton';
import Slide from '@material-ui/core/Slide';
import TextField from '@material-ui/core/TextField';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import axios from 'axios';
import React from 'react';

const styles = theme => ({
    button: {
        margin: 10,
        float: 'right'
    },
    appBar: {
        position: 'relative',
    },
    title: {
        marginLeft: theme.spacing(2),
        flex: 1,
    }
});

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

class IntroductionEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            open: false,
            idx: '',
            introductionTitle: "",
            title1: "",
            content1: "",
            title2: "",
            content2: "",
            title3: "",
            content3: "",
            title4: "",
            content4: "",
            title5: "",
            content5: "",
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleFormSave = (e) => {
        e.preventDefault()

    }

    editIntroduction = () => {
        const no = this.state.idx;
        axios.put('http://localhost:8080/api/introductions/' + no, this.state)
            .then(res => {
                console.log(res);
            })
    }

    handleClickOpen = () => {
        this.setState({
            open: true,
            idx: this.props.idx,
            introductionTitle: this.props.introductionTitle,
            title1: this.props.title1,
            content1: this.props.content1,
            title2: this.props.title2,
            content2: this.props.content2,
            title3: this.props.title3,
            content3: this.props.content3,
            title4: this.props.title4,
            content4: this.props.content4,
            title5: this.props.title5,
            content5: this.props.content5,
        })
    }

    handleClose = (event) => {
        console.log(this.state)
        this.editIntroduction();
        this.setState({
            open: false
        })
    }

    handleChange = (event) => {
        const target = event.target
        const name = target.name
        const value = target.value

        this.setState({
            [name]: value
        });
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button color="primary" className={classes.button} onClick={this.handleClickOpen}>
                    edit
                </Button>
                <Dialog
                    fullWidth
                    maxWidth="lg"
                    open={this.state.open}
                    onClose={this.handleClose}
                    TransitionComponent={Transition}
                >
                    <AppBar className={classes.appBar}>
                        <Toolbar>
                            <IconButton edge="start" color="inherit" onClick={this.handleClose} aria-label="close">
                                <CloseIcon />
                            </IconButton>
                            <Typography variant="h6" className={classes.title}>
                                <TextField label="Company" value={this.state.introductionTitle} autoFocus margin="dense" name="introductionTitle" onChange={this.handleChange} />
                            </Typography>
                            <Button color="inherit" onClick={this.handleClose}>
                                save
                            </Button>
                        </Toolbar>
                    </AppBar>
                    <div className={classes.textField}>
                        <TextField label="Title" autoFocus margin="auto" name="title1" value={this.state.title1} onChange={this.handleChange} />
                        <br />
                        <TextField label="Content" fullWidth autoFocus margin="auto" name="content1" value={this.state.content1} onChange={this.handleChange} />
                    </div>
                    <div className={classes.textField}>
                        <TextField label="Title" autoFocus margin="dense" name="title2" value={this.state.title2} onChange={this.handleChange} />
                        <br />
                        <TextField label="Content" fullWidth autoFocus margin="dense" name="content2" value={this.state.content2} onChange={this.handleChange} />
                    </div>
                    <div className={classes.textField}>
                        <TextField label="Title" autoFocus margin="dense" name="title3" value={this.state.title3} onChange={this.handleChange} />
                        <br />
                        <TextField label="Content" fullWidth autoFocus margin="dense" name="content3" value={this.state.content3} onChange={this.handleChange} />
                    </div>
                    <div className={classes.textField}>
                        <TextField label="Title" autoFocus margin="dense" name="title4" value={this.state.title4} onChange={this.handleChange} />
                        <br />
                        <TextField label="Content" fullWidth autoFocus margin="dense" name="content4" value={this.state.content4} onChange={this.handleChange} />
                    </div>
                    <div className={classes.textField}>
                        <TextField label="Title" autoFocus margin="dense" name="title5" value={this.state.title5} onChange={this.handleChange} />
                        <br />
                        <TextField label="Content" fullWidth autoFocus margin="dense" name="content5" value={this.state.content5} onChange={this.handleChange} />
                    </div>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(IntroductionEdit);