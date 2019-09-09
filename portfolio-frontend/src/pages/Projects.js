import React from 'react';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import Project from '../components/Project';

const project = [{
    'idx': 1,
    'projectName': 'Smart Pot',
    'description' : '스마트 화분',
    'persons' : '박동현',
    'period' : '3개월',
    'registerDate' : '2019-08-24'
},
{
    'idx': 2,
    'projectName': 'Portfolio',
    'description' : '포트폴리오',
    'persons' : '박동현',
    'period' : '4개월',
    'registerDate' : '2019-08-24'
}]

class Projects extends React.Component {
    render() {
        const cellList = ["No", "Project Name", "Description", "Persons", "Period", "RegisterDate"]
        return (
            <div >
                <Table >
                    <TableHead>
                        <TableRow>
                            {cellList.map(c => {
                                return <TableCell >{c}</TableCell>
                            })}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                            {project.map(c => {return <Project no={c.idx} key={c.idx} projectName={c.projectName} description={c.description} persons={c.persons} period={c.period} registerDate={c.registerDate} />})}
                    </TableBody>
                </Table>
            </div>
        );
    }
}

export default Projects;