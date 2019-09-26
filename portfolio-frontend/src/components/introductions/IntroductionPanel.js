import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import IntroductionEdit from './IntroductionEdit';
import IntroductionDelete from './IntroductionDelete';



const styles = theme => ({
    panel: {
        marginBottom: '5px'
    },
    root: {
        width: '100%',
    },
    card: {
        minWidth: '100%',
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    button: {
        margin: 10,
        float: 'right'
    }
});
class Introduction extends React.Component {
    constructor(props) {
        super();
        this.state = {
            title: "",
            reason: "",
            strength: "",
            weakness: "",
            aspiration: "",
        }
    }

    componentDidMount = () => {
        this.setState({
            title: this.props.title,
            growth: this.props.growth,
            reason: this.props.reason,
            strength: this.props.strength,
            weakness: this.props.weakness,
            aspiration: this.props.aspiration
        })
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <div className={classes.root}>
                    <ExpansionPanel className={classes.panel}>
                        <ExpansionPanelSummary
                            expandIcon={<ExpandMoreIcon />}
                            aria-controls="panel1a-content"
                            id="panel1a-header">
                            <Typography variant="h5" component="h2" >{this.state.title}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <div className={classes.card}>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            성장 과정
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.state.growth}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            지원 동기
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.state.reason}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            장점
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.state.strength}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            단점
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.state.weakness}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            입사 후 포부
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.state.aspiration}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <IntroductionDelete
                                    idx={this.props.idx}
                                    title={this.props.title} />
                                <IntroductionEdit
                                    key={this.props.idx}
                                    idx={this.props.idx}
                                    title={this.state.title}
                                    growth={this.state.growth}
                                    reason={this.state.reason}
                                    strength={this.state.strength}
                                    weakness={this.state.weakness}
                                    aspiration={this.state.aspiration}
                                />
                            </div>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                </div>
            </div >
        );
    }
}

export default withStyles(styles)(Introduction);