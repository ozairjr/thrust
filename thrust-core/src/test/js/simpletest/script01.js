const Thread = Java.type('java.lang.Thread')
const ThrustContextAPI = Java.type('br.com.softbox.thrust.api.ThrustContextAPI')
console.log('script01: running...')
Thread.sleep(1001)
ThrustContextAPI.setValue('simple-value', 'A string from other context')
console.log('script01: ended.')
