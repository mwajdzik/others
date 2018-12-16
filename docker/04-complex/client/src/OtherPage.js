import React from 'react';
import { Link } from 'react-router-dom';

export default () => {
    return (
        <div>
            In some other page!<br/>
            <Link to="/">Go back home</Link>
        </div>
    );
};
