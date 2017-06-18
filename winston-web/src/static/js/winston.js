var $messageInput = $('.message-input');

var $messages = $('.messages-content'),
    d, h, m, scrollPct;
i = 0;

var $status = $('.connection-status');

function setStatus(status) {
    switch (status.toLowerCase()) {
        case 'pending':
            status = 'connecting';
            break;
        default:
            break;
    }
    $status.html(status);
}

function updateScrollbar() {
    $messages.mCustomScrollbar('scrollTo', 'bottom', {
        scrollEasing: 'easeOut'
    });
}

function buildInputMessage(content) {
    return newMessage($('<div class="message message-personal">' + content + '</div>').appendTo($('.mCSB_container')).addClass('new'));
}

function buildOutputMessage(content) {
    return newMessage($('<div class="message new"><div class="avatar default"></div>' + content + '</div>').appendTo($('.mCSB_container')).addClass('new'));
}

function buildLoadingMessage() {
    return newMessage($('<div class="message loading new"><div class="avatar default"></div><span></span></div>').appendTo($('.mCSB_container')));
}

function newMessage(message) {
    updateScrollbar();
    return message;
}

function setDate(message) {
    d = new Date();
    h = d.getHours();
    m = d.getMinutes();
    if (h < 10) {
        h = '0' + h;
    }
    if (m < 10) {
        m = '0' + m;
    }
    if (message === undefined) {
        message = $('.message:last');
    }
    $('<div class="timestamp">' + h + ':' + m + '</div>').appendTo(message);
}

function insertMessage() {
    $messageInput.focus();
    content = $('<?>' + $messageInput.val() + '</?>').text();
    if ($.trim(content) === '') {
        return false;
    }
    $messageInput.val(null);
    var bUpdateScrollbar = (scrollPct === 100);
    var message = buildInputMessage(content);
    setDate(message);
    if (bUpdateScrollbar) {
        updateScrollbar();
    }
    Winston.send(content);
}

$('.message-submit').on('click', function () {
    insertMessage();
});

$(window).on('keydown', function (e) {
    if (e.which === 13) {
        if ($messageInput.is(':focus')) {
            insertMessage();
        }
        $messageInput.focus();
        return false;
    }
    else if (e.which === 9) {
        $messageInput.focus();
        return false;
    }
});

$(window).on('load', function () {
    document.title = 'Winston - Engage';
    console.info('Winston: initialised.');
    $messages.mCustomScrollbar({
        scrollInertia: 0,
        alwaysShowScrollbar: 1,
        autoHideScrollbar: true,
        callbacks: {
            onScroll: function () {
                scrollPct = this.mcs.topPct;
            }
        }
    });
    $messageInput.focus();
    $(function () {
        $messageInput.on('input', function (e) {
            $(this).val($(this).val().slice(0, 1).toUpperCase() + $(this).val().slice(1));
        });
    });
    buildOutputMessage('<i>Good day, sir.</i><br>My name is <b>Winston</b>, how may I help you?');
    Winston.init();
});

Winston = function () {
};

Winston.nextConversationReference = function () {
    var alphabet = '0123456789abcdefghijklmnopqrstuvwxyz'.split('');
    var chars = [];
    for (var stringIndex = 0; stringIndex < str.length; stringIndex++) {
        chars.push(alphabet.indexOf(str[stringIndex]));
    }
    for (var charIndex = chars.length - 1; charIndex >= 0; charIndex--) {
        var tmp = chars[charIndex];
        if (tmp >= 0 && tmp < 25) {
            chars[charIndex]++;
            break;
        }
        else {
            chars[charIndex] = 0;
        }
    }
    var conversationReference = '';
    for (var i = 0; i < chars.length; i++) {
        conversationReference += alphabet[chars[i]];
    }
    return conversationReference;
};

Winston.conversationReference = 0;
Winston.awaitingMessages = [];
Winston.send = function (content) {
    var inputMessage = {
        conversationReference: '' + Winston.conversationReference++,
        content: content
    };
    Winston.awaitingMessages.push({
        date: Date.now(),
        //loadingMessage: buildLoadingMessage(),
        inputMessage: inputMessage
    });
    Winston.websocket.send(JSON.stringify(inputMessage));
};

Winston.receive = function (data) {
    buildOutputMessage(data.content);
};

Winston.init = function () {
    setStatus('pending');
    if ('WebSocket' in window) {
        var location = window.location, URI;
        if (location.protocol === "https:") {
            URI = "wss:";
        } else {
            URI = "ws:";
        }
        URI += "//" + location.host;
        URI += location.pathname + "/ws/data/message/";
        Winston.websocket = new WebSocket(URI);
        Winston.websocket.onopen = function (event) {
            setStatus('online');
            console.info('WebSocket has been opened.');
        };
        Winston.websocket.onmessage = function (event) {
            Winston.receive(JSON.parse(event.data));
        };
        Winston.websocket.onclose = function (event) {
            var reason;
            if (event.code === 1000)
                reason = 'Normal closure, meaning that the purpose for which the connection was established has been fulfilled.';
            else if (event.code === 1001)
                reason = 'An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.';
            else if (event.code === 1002)
                reason = 'An endpoint is terminating the connection due to a protocol error';
            else if (event.code === 1003)
                reason = 'An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).';
            else if (event.code === 1004)
                reason = 'Reserved. The specific meaning might be defined in the future.';
            else if (event.code === 1005)
                reason = 'No status code was actually present.';
            else if (event.code === 1006)
                reason = 'The connection was closed abnormally, e.g., without sending or receiving a Close control frame';
            else if (event.code === 1007)
                reason = 'An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).';
            else if (event.code === 1008)
                reason = 'An endpoint is terminating the connection because it has received a message that "violates its policy". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.';
            else if (event.code === 1009)
                reason = 'An endpoint is terminating the connection because it has received a message that is too big for it to process.';
            else if (event.code === 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
                reason = 'An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn\'t return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: ' + event.reason;
            else if (event.code === 1011)
                reason = 'A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.';
            else if (event.code === 1015)
                reason = 'The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can\'t be verified).';
            else
                reason = 'Unknown reason';
            console.warn('The WebSocket connection was closed for reason: ' + reason + '');
            setStatus('offline');
        };
        Winston.websocket.onerror = function (event) {
            console.error('A WebSocket error has occurred.')
        }
    }
    else {
        console.warn('WebSocket is not supported by your browser.');
    }
};
