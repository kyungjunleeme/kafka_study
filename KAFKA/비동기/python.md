

p.126 쪽
```
Future objects are the result of asynchronous operations, and 

1) they have methods for check‐ing the status of the asynchronous operation, 
2) canceling it, 
3) waiting for it to complete,
4) and executing functions after its completion.

Kafka’s AdminClient wraps the Future objects into Result objects, which provide methods to wait for the operation to com‐
plete and helper methods for common follow-up operations.
```


```python
class Task(Future):
    """ A coroutine wrapped in a Future. """

    1. 상태 확인
    # 작업이 완료되었는지(True) 여부를 반환합니다.
    def done(self, *args, **kwargs): # real signature unknown
        """
        Return True if the future is done.
        
        Done means either that a result / exception are available, or that the
        future was cancelled.
        """
        pass
    
    2. 취소
    # 작업이 취소되었는지 여부를 반환합니다.
    def cancelled(self, *args, **kwargs): # real signature unknown
        """ Return True if the future was cancelled. """
        pass

    # 작업을 취소하도록 요청하고, CancelledError 예외를 발생시킵니다.
    def cancel(self): # real signature unknown; restored from __doc__
        """
        Request that this task cancel itself.
        
        This arranges for a CancelledError to be thrown into the
        wrapped coroutine on the next cycle through the event loop.
        The coroutine then has a chance to clean up or even deny
        the request using try/except/finally.
        
        Unlike Future.cancel, this does not guarantee that the
        task will be cancelled: the exception might be caught and
        acted upon, delaying cancellation of the task or preventing
        cancellation completely.  The task may also return a value or
        raise a different exception.
        
        Immediately after this method is called, Task.cancelled() will
        not return True (unless the task was already cancelled).  A
        task will be marked as cancelled when the wrapped coroutine
        terminates with a CancelledError exception (even if cancel()
        was not called).
        
        This also increases the task's count of cancellation requests.
        """
        pass
    
    3. 대기
    # 작업이 완료된 결과를 반환합니다. 예외가 발생하면 예외를 다시 일으킵니다.
    def result(self, *args, **kwargs): # real signature unknown
        """
        Return the result this future represents.
        
        If the future has been cancelled, raises CancelledError.  If the
        future's result isn't yet available, raises InvalidStateError.  If
        the future is done and has an exception set, this exception is raised.
        """
        pass
    
    
    # 작업에서 발생한 예외를 반환합니다. 예외가 없으면 None을 반환합니다.
    def exception(self, or_None_if_no_exception_was_set): # real signature unknown; restored from __doc__
        """
        Return the exception that was set on this future.
        
        The exception (or None if no exception was set) is returned only if
        the future is done.  If the future has been cancelled, raises
        CancelledError.  If the future isn't done yet, raises
        InvalidStateError.
        """
        pass

    # await 사용 -> 작업이 완료될 때까지 기다리고, 결과를 반환합니다.
    
    
    4. 콜백 실행
    # 작업 완료 후 특정 함수를 실행하도록 설정할 수 있습니다.
    def add_done_callback(self, *args, **kwargs): # real signature unknown
        """
        Add a callback to be run when the future becomes done.
        
        The callback is called with a single argument - the future object. If
        the future is already done when this is called, the callback is
        scheduled with call_soon.
        """
        pass

```