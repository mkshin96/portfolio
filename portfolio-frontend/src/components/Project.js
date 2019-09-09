import React from 'react';
import TableRow from '@material-ui/core/TableRow'
import TableCell from '@material-ui/core/TableCell'

class Project extends React.Component {
    render() {
        return (
            <TableRow>
                <TableCell>{this.props.no}</TableCell>
                <TableCell>{this.props.projectName}</TableCell>
                <TableCell>{this.props.description}</TableCell>
                <TableCell>{this.props.persons}</TableCell>
                <TableCell>{this.props.period}</TableCell>
                <TableCell>{this.props.registerDate}</TableCell>
            </TableRow>
        );
    }
}

export default Project;