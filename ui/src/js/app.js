import React from 'react';
import HelloMessage from './components/hello';

const container = document != null ? document.getElementById('hello-container') : null

if (container) {
  React.render(
    <HelloMessage name="John (from browser)" />,
    container
  )
}
