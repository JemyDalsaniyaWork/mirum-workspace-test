import React from 'react';
import ReactDOM from 'react-dom';
import '@mobiscroll/react/dist/css/mobiscroll.min.css';
import { Datepicker, Page} from '@mobiscroll/react';


const myLabels = React.useMemo(() => {
	return [{
		start: '2023-12-12',
		textColor: '#e1528f',
		title: '2 SPOTS'
	}];
}, []);

const myInvalid = React.useMemo(() => {
	return [{
		start: '2023-12-12T08:00',
		end: '2023-12-12T13:00'
	}, {
		start: '2023-12-12T15:00',
		end: '2023-12-12T17:00'
	}, {
		start: '2023-12-12T19:00',
		end: '2023-12-12T20:00'
	}];
}, []);

// const onPageLoadingDatetime = React.useCallback((event, inst) => {
// 	getDatetimes(event.firstDay, (bookings) => {
// 		setDatetimeLabels(bookings.labels);
// 		setDatetimeInvalid(bookings.invalid);
// 	});
// }, []);
//
// const getDatetimes = (d, callback) => {
// 	let invalid = [];
// 	let labels = [];
//
// 	getJson('https://trial.mobiscroll.com/getbookingtime/?year=' + d.getFullYear() + '&month=' + d.getMonth(), (bookings) => {
// 		for (let i = 0; i < bookings.length; ++i) {
// 			const booking = bookings[i];
// 			const bDate = new Date(booking.d);
//
// 			if (booking.nr > 0) {
// 				labels.push({
// 					start: bDate,
// 					title: booking.nr + ' SPOTS',
// 					textColor: '#e1528f'
// 				});
// 				invalid = [...invalid, ...booking.invalid];
// 			} else {
// 				invalid.push(d);
// 			}
// 		}
// 		callback({ labels: labels, invalid: invalid });
// 	}, 'jsonp');
// }

return (
	<Page className="md-calendar-booking">
		<div className="mbsc-form-group">
			<div className="mbsc-form-group-title">Select date & time</div>
			<Datepicker
				display="inline"
				controls={['calendar', 'timegrid']}
				min={min}
				max={max}
				minTime="08:00"
				maxTime="19:59"
				stepMinute={60}
				width={null}
				labels={myLabels}
				invalid={myInvalid}
				//onPageLoading={onPageLoadingDatetime}
				cssClass="booking-datetime"
			/>
		</div>
	</Page>
);
// <Datepicker
// 	controls={['calendar', 'timegrid']}
// 	min="2023-12-12T00:00"
// 	max="2024-06-12T00:00"
// 	minTime="08:00"
// 	maxTime="19:59"
// 	stepMinute={60}
// 	labels={myLabels}
// 	invalid={myInvalid}
// />

// class Square extends React.Component {
// 	render() {
// 		return (
// 			<button className='square' onClick={() => this.props.onClick()}>
// 				{this.props.value}
// 			</button>
// 		);
// 	}
// }
//
// class Board extends React.Component {
// 	constructor() {
// 		super();
//
// 		this.state = {
// 			squares: Array(9).fill(null),
// 			xIsNext: true
// 		};
// 	}
//
// 	handleClick(i) {
// 		const squares = this.state.squares.slice();
//
// 		if (calculateWinner(squares) || squares[i]) {
// 			return;
// 		}
//
// 		squares[i] = this.state.xIsNext ? 'X' : 'O';
//
// 		this.setState({squares: squares, xIsNext: !this.state.xIsNext});
// 	}
//
// 	renderSquare(i) {
// 		return (
// 			<Square
// 				value={this.state.squares[i]}
// 				onClick={() => this.handleClick(i)}
// 			/>
// 		);
// 	}
//
// 	render() {
// 		const winner = calculateWinner(this.state.squares);
// 		let status;
//
// 		if (winner) {
// 			status = 'Winner: ' + winner;
// 		} else {
// 			status = 'Next player: ' + (this.state.xIsNext ? 'X' : 'O');
// 		}
//
// 		return (
// 			<div>
// 				<div className='status'>{status}</div>
// 				<div className='board-row'>
// 					{this.renderSquare(0)}
// 					{this.renderSquare(1)}
// 					{this.renderSquare(2)}
// 				</div>
// 				<div className='board-row'>
// 					{this.renderSquare(3)}
// 					{this.renderSquare(4)}
// 					{this.renderSquare(5)}
// 				</div>
// 				<div className='board-row'>
// 					{this.renderSquare(6)}
// 					{this.renderSquare(7)}
// 					{this.renderSquare(8)}
// 				</div>
// 			</div>
// 		);
// 	}
// }
//
// class Game extends React.Component {
// 	render() {
// 		return (
// 			<div className='game'>
// 				<div className='game-board'>
// 					<Board />
// 				</div>
// 				<div className='game-info'>
// 					<div>{/* status */}</div>
// 					<ol>{/* TODO */}</ol>
// 				</div>
// 			</div>
// 		);
// 	}
// }
//
// function calculateWinner(squares) {
// 	const lines = [
// 		[0, 1, 2],
// 		[3, 4, 5],
// 		[6, 7, 8],
// 		[0, 3, 6],
// 		[1, 4, 7],
// 		[2, 5, 8],
// 		[0, 4, 8],
// 		[2, 4, 6]
// 	];
//
// 	for (let i = 0; i < lines.length; i++) {
// 		const [a, b, c] = lines[i];
//
// 		if (
// 			squares[a] &&
// 			squares[a] === squares[b] &&
// 			squares[a] === squares[c]
// 		) {
// 			return squares[a];
// 		}
// 	}
// 	return null;
// }
//
// export default function(elementId) {
// 	ReactDOM.render(<Game />, document.getElementById(elementId));
// }

