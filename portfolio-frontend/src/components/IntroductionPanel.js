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
            title: "123"
        }
    }

    componentDidMount = () => {
        console.log(this.props)
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
                            <Typography variant="h5" component="h2" >{this.props.title}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <div className={classes.card}>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            성장 과정
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.props.growth}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            지원 동기
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.props.reason}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            장점
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.props.strength}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            단점
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.props.weakness}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <Card className={classes.card}>
                                    <CardContent>
                                        <Typography variant="h6" component="h2">
                                            입사 후 포부
                                    </Typography>
                                        <Typography variant="body2" component="p">
                                            {this.props.aspiration}
                                        </Typography>
                                    </CardContent>
                                </Card>
                                <IntroductionEdit
                                    key={this.props.idx}
                                    title={this.props.title}
                                    growth={this.props.growth}
                                    reason={this.props.reason}
                                    strength={this.props.strength}
                                    weakness={this.props.weakness}
                                    aspiration={this.props.aspiration}
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