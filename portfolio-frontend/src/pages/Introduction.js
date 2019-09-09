import React from 'react';
import IntroductionPanel from '../components/IntroductionPanel';
import * as service from '../service/introduction';


class Introduction extends React.Component {

    constructor(props) {
        super();
        this.state = {
            introduction: ""
        }
    }

    componentDidMount() {
        this.fetchIntroductionInfo();
    }

    fetchIntroductionInfo = async () => {
        const introduction = await service.getIntroduction();
        this.setState({
            'introductions': introduction.data._embedded.introductionList
        });
    }

    render() {
        return (
            <div>
                {
                    this.state.introductions ?
                    this.state.introductions.map(c => {
                        return (<IntroductionPanel
                            key={c.idx}
                            title={c.title}
                            growth={c.growth}
                            reason={c.reason}
                            strength={c.strength}
                            weakness={c.weakness}
                            aspiration={c.aspiration}
                        />
                        )
                    }) : ""
                }
            </div >
        );
    }
}

export default Introduction;